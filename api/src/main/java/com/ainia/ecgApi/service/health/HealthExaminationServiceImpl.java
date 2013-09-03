package com.ainia.ecgApi.service.health;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.utils.StringUtils;
import com.ainia.ecgApi.dao.health.HealthExaminationDao;
import com.ainia.ecgApi.domain.charge.Card;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.domain.health.HealthRule;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.ainia.ecgApi.domain.health.HealthRuleReply;
import com.ainia.ecgApi.domain.sys.Operator;
import com.ainia.ecgApi.domain.sys.SystemConfig;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.domain.task.Task.Status;
import com.ainia.ecgApi.dto.health.HealthInfo;
import com.ainia.ecgApi.service.charge.CardService;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;
import com.ainia.ecgApi.service.sys.OperatorService;
import com.ainia.ecgApi.service.sys.SystemConfigService;
import com.ainia.ecgApi.service.sys.UserService;
import com.ainia.ecgApi.service.task.ExaminationTaskService;
import com.ainia.ecgApi.service.task.TaskService;
import com.ainia.ecgApi.utils.DataProcessor;
import com.ainia.ecgApi.utils.ECGChart;
import com.ainia.ecgApi.utils.OxygenChart;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * <p>HealthExamination Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationServiceImpl.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Service
public class HealthExaminationServiceImpl extends BaseServiceImpl<HealthExamination , Long> implements HealthExaminationService {
    
    @Autowired
    private HealthExaminationDao healthExaminationDao;
    @Autowired
    private HealthReplyService healthReplyService;
    @Autowired
    private HealthRuleReplyService healthRuleReplyService;
    @Autowired
    private ExaminationTaskService examinationTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private HealthRuleService healthRuleService;
    @Autowired
    private CardService cardService;
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private UserService userService;
    
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    
    
    @Override
    public BaseDao<HealthExamination , Long> getBaseDao() {
        return healthExaminationDao;
    }

	public void reply(Long id , HealthReply reply) {
		healthReplyService.create(reply);
	}

	// 判断用户是否已经充值
	private boolean canFreeReply(final HealthExamination examination) {
		// 判断系统是否允许免费使用
		String config = systemConfigService.findByKey(SystemConfig.EXAMINATION_REPLY_FREE);
		boolean isFree = config == null ? false : Boolean.valueOf(config);
		if (isFree) { return true; }

		AuthUser authUser = authenticateService.getCurrentUser();	
		// 判断用户是否充值以及在服务时间
		Query<Card> query = new Query<Card>();
		query.isNotNull(Card.ACTIVED_DATE);
		query.isNotNull(Card.USER_ID);
		query.eq(Card.USER_ID, authUser.getId());
		query.addOrder(Card.ACTIVED_DATE, OrderType.desc);
		List<Card> cards = cardService.findAll(query);

		if (cards == null || cards.size() == 0) { return false; }

		int inner = 0;
		GregorianCalendar gc = new GregorianCalendar();
		Date now = new Date();
		for (Card card: cards) {
			Date endDate;
			gc.setTime(card.getActivedDate());
			gc.add(GregorianCalendar.DATE, card.getDays());
			endDate = gc.getTime();
			if (card.getActivedDate().before(now) && endDate.after(now)) {
				inner++;
			}
		}
		if (inner > 0) { return true; }

		return false;
	}

	// 接线员已经和专家绑定
	private boolean isOpAndExpLinked () {
		boolean linked = false;
		// 如果不是自动回复
		String config = systemConfigService.findByKey(SystemConfig.EXAMINATION_REPLY_AUTO);
		boolean isAuto = config == null ? false : Boolean.valueOf(config);
		if (isAuto) { return true; }
		
		// 查看
		List <Operator> operators = operatorService.findAll(new Query());
		for (Operator operator  : operators) {
			if (operator.getExperts().size() > 0) {
				linked = true;
				break;
			}
		}
		return linked;
	}

	private boolean autoreply(final HealthExamination examination) {
		boolean isReplied = false;

		//判断是否由于自动回复导致已回复
		String config = systemConfigService.findByKey(SystemConfig.EXAMINATION_REPLY_AUTO);
		boolean isAuto = config == null ? false : Boolean.valueOf(config);
		
		List<HealthRule> filters = healthRuleService.findAllFiltersByUser(examination.getUserId());

		if (filters == null) { return isReplied; }
			
		for (HealthRule rule : filters) {
			if (rule.isMatch(examination)) {
				// 数据状态
				if (examination.getLevel() == null) {
					examination.setLevel(rule.getLevel());
				} else if (examination.getLevel().compareTo(rule.getLevel()) < 0) {
					examination.setLevel(rule.getLevel());
				}

				if (!isAuto) {
					continue;
				}
				
		    	Query<HealthRuleReply> repliyConfgQuery = new Query <HealthRuleReply> ();
		    	repliyConfgQuery.eq(HealthRuleReply.RULE_ID , rule.getId());
				List <HealthRuleReply> repliyConfgs = healthRuleReplyService.findAll(repliyConfgQuery);
				if (repliyConfgs != null && repliyConfgs.size() > 0) {
					// 获得预设
					HealthRuleReply replyConfig = repliyConfgs.get(0);
					
					isReplied = true;
					
					// 设置reply
					HealthReply reply = new HealthReply();
					reply.setResult(replyConfig.getResult());
					reply.setContent(replyConfig.getContent());
					reply.setLevel(rule.getLevel());
					reply.setReason("系统自动回复");
					reply.setExaminationId(examination.getId());
					healthReplyService.create(reply);
				}
				
			}
		}

		return isReplied;
	}
	
	private void updateTaskAndExamination(final ExaminationTask task, final HealthExamination examination) {
		if (autoreply(examination)) {
			taskService.complete(task); 
		} else {
			taskService.pending(task); 
		}
		update(examination);
	}

	public void upload(final HealthExamination examination , final byte[] gzipedUploadData , String md5) {

		// 判断是否有效登录
		final AuthUser authUser = authenticateService.getCurrentUser();	
		if (authUser == null) {
			throw new ServiceException("authUser.error.notFound");
		}
		if (!User.class.getSimpleName().equals(authUser.getType())) {
			throw new ServiceException("examination.error.upload.notAllowed");
		}
		
		// 判断是否有接线员已经和专家绑定
		if (!isOpAndExpLinked()) {
			throw new ServiceException("none.operator.is.linked.with.expert");
		}
		
		// 判断是否是测试请求
		boolean isTest = examination.getIsTest() == null ? false : examination.getIsTest();
		if (gzipedUploadData == null && !isTest) {
			throw new ServiceException("file.is.empty");
		} else if (gzipedUploadData != null && gzipedUploadData.length == 0) {
			throw new ServiceException("file.length.is.zero");
		}

		// 判断是否用户可以免费使用
		if (!canFreeReply(examination)) {
			throw new ServiceException("examination.reply.is.not.free");
		}
		// 校验gzip是否正确
		if (examination.getIsGziped()) {
			try {
				new GZIPInputStream(new ByteArrayInputStream(gzipedUploadData));
			} catch(Exception e) {
				throw new ServiceException("examination.data.not.in.ziped.format");
			}
		}
		/*
		//校验MD5值
		if (StringUtils.isNotBlank(md5)) {
			BigInteger bigint = new BigInteger(1 , DigestUtils.md5(uploadData));
			String md5Value = bigint.toString(16);
			if (!md5Value.equals(md5)) {
				if (log.isWarnEnabled()) {
					log.warn(" the upload file md5 is not valid");
				}
			}
		}*/
		
		examination.setLevel(Level.outside);
		examination.setId(null); // 防止被 examination的id被注入
		examination.setUserId(authUser.getId());
		examination.setUserName(authUser.getUsername());
		examination.setUserType(authUser.getType());
		examination.setTestItem("mobile");
		if (examination.getMedicine() != null) {
			try {
				examination.setMedicine(URLDecoder.decode(examination.getMedicine(), "UTF-8"));
			}
			catch(UnsupportedEncodingException e) {
				throw new ServiceException("examination.medicine.decode.error");
			}
			
		}
		this.create(examination);
		
		final ExaminationTask task = new ExaminationTask();
		task.setExaminationId(examination.getId());
		task.setAuto(false); // 只有真正回复了才设置成true
		task.setUserId(authUser.getId());
		task.setUserName(authUser.getName());
		task.setApkId(examination.getApkId());
		task.setStatus(Status.draft);
		taskService.create(task);

		if (!isTest) {
			executorService.execute(new Runnable(){

				public void run() {
					try {
						byte[] uploadData = new byte[0];

						
						try {
							if (examination.getIsGziped()) {
								//存储数据包
								String zipPath = "user/" +  String.valueOf(authUser.getId()) + "/examination/" + examination.getId() + "/zip";
								uploadService.save(Type.heart_img , zipPath , gzipedUploadData);

								// decompress the file
								GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(gzipedUploadData));

								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								int count;
								byte data[] = new byte[200];
								while ((count = gis.read(data, 0, 200)) != -1) {
									baos.write(data, 0, count);
								}
								uploadData = baos.toByteArray();
							} else {
								uploadData = gzipedUploadData;
							}
						} catch(Exception e) {
							e.printStackTrace();
						}

						//存储原始数据
						String rawPath = "user/" +  String.valueOf(authUser.getId()) + "/examination/" + examination.getId() + "/raw";
						String rawUri = uploadService.save(Type.heart_img , rawPath , uploadData);
						
						examination.setHeartData(rawUri);
						
						// 解析数据
				    	DataProcessor processor = new DataProcessor();
				    	processor.process(uploadData , uploadData.length);
						float[] daolian = processor.getDaolian_i();
						
						// 生成文件相对路径
						daolian = processor.getDaolian_i();
						String ecg1Path = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg1.jpg";
						
						byte[] ecg1 = ECGChart.createChart(
								"ECG I",
								daolian, processor.getMaxDaolian(), 0,
								daolian.length, (int)(daolian.length*0.756), (int)37.8*8);
						uploadService.save(Type.heart_img, ecg1Path, ecg1);

						daolian = processor.getDaolian_ii();
						String ecg2Path = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg2.jpg";
						byte[] ecg2 = ECGChart.createChart(
								"ECG II",
								daolian, processor.getMaxDaolian(), 0,
								daolian.length, (int)(daolian.length*0.756), (int)37.8*8);
						uploadService.save(Type.heart_img, ecg2Path, ecg2);

						daolian = processor.getDaolian_iii();
						String ecg3Path = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg3.jpg";
						byte[] ecg3 = ECGChart.createChart(
								"ECG III",
								daolian, processor.getMaxDaolian(), 0,
								daolian.length, (int)(daolian.length*0.756), (int)37.8*8);
						uploadService.save(Type.heart_img, ecg3Path, ecg3);

						daolian = processor.getDaolian_avr();
						String ecg4Path = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg4.jpg";
						byte[] ecg4 = ECGChart.createChart(
								"ECG aVR",
								daolian, processor.getMaxDaolian(), 0,
								daolian.length, (int)(daolian.length*0.756), (int)37.8*8);
						uploadService.save(Type.heart_img, ecg4Path, ecg4);

						daolian = processor.getDaolian_avl();
						String ecg5Path = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg5.jpg";
						byte[] ecg5 = ECGChart.createChart(
								"ECG aVL",
								daolian, processor.getMaxDaolian(), 0,
								daolian.length, (int)(daolian.length*0.756), (int)37.8*8);
						uploadService.save(Type.heart_img, ecg5Path, ecg5);

						daolian = processor.getDaolian_avf();
						String ecg6Path = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg6.jpg";
						byte[] ecg6 = ECGChart.createChart(
								"ECG aVF",
								daolian, processor.getMaxDaolian(), 0,
								daolian.length, (int)(daolian.length*0.756), (int)37.8*8);
						uploadService.save(Type.heart_img, ecg6Path, ecg6);

						daolian = processor.getDaolian_v();
						String ecg7Path = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg7.jpg";
						byte[] ecg7 = ECGChart.createChart(
								"ECG V",
								daolian, processor.getMaxDaolian(), 0,
								daolian.length, (int)(daolian.length*0.756), (int)37.8*8);
						uploadService.save(Type.heart_img, ecg7Path, ecg7);		

						byte[] oxygenData = processor.getOxygenData();
						int oxyLen = processor.getOxygenDataLen();
						String oxyPath = "user/"
								+ String.valueOf(authUser.getId())
								+ "/examination/" + examination.getId()
								+ "/ecg8.jpg";
						byte[] oxyChart = OxygenChart.createChart(oxygenData, 0, oxyLen, oxyLen*10, (int)37.8*8);
						uploadService.save(Type.heart_img, oxyPath, oxyChart);
						
						// 获得医疗数据
						HealthInfo hi = processor.getHealthInfo();
						examination.setHeartRhythm(hi.heartrate);
						examination.setBloodPressureLow(hi.dbp);
						examination.setBloodPressureHigh(hi.sbp);
						examination.setPulserate(hi.pulserate);
						examination.setBloodOxygen(hi.oxygen);
						
						// 根据医疗数据做后续处理,如自动回复
						updateTaskAndExamination(task, examination);
						
						if (processor.isSnerror() && log.isWarnEnabled()) {
							log.warn("sn error!");
						}
					}
					catch(Exception e) {
						examination.setHasDataError(true);
						updateTaskAndExamination(task, examination);
					}
				}
				
			});		
		} else {
			updateTaskAndExamination(task, examination);
		}
	}

	@Override
	public List<Map> statisticsByUserAndDay(Long userId, Date start, Date end) {
		List<Object[]> list = healthExaminationDao.statisticsByUserAndDay(userId, start, end);
		List<Map> results = new ArrayList <Map>(list.size());
		try {
			//TODO 暂时使用日期方式转换 处理日期格式化
			SimpleDateFormat from = new SimpleDateFormat("yyyy-M-d");
			SimpleDateFormat to   = new SimpleDateFormat("yyyy-MM-dd");
			for (Object[] data : list) {
				Map<String, Object> dataMap = new HashMap<String , Object>();
				dataMap.put(HealthExamination.BLOOD_PRESSURE_LOW , data[0]);
				dataMap.put(HealthExamination.BLOOD_PRESSURE_HIGH , data[1]);
				dataMap.put(HealthExamination.HEART_RHYTHM , data[2]);
				dataMap.put(HealthExamination.BLOOD_OXYGEN , data[3]);
				dataMap.put(HealthExamination.BREATH , data[4]);
				dataMap.put(HealthExamination.BODY_TEMP , data[5]);
				dataMap.put(HealthExamination.PULSERATE , data[6]);
				Date date = from.parse((String)data[7]);
				dataMap.put(HealthExamination.CREATED_DATE , to.format(date));
				results.add(dataMap);
 			}
		}
		catch(Exception e) {
			throw new ServiceException(e , "exception.examination.statisticsError");
		}
		return results;
	}
	
	@Override
	public void exportPDF(HealthExamination examination , OutputStream output) {
		try {
			User user = userService.get(examination.getUserId());
			
			Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter writer = PdfWriter.getInstance(doc, output);
			doc.open();
			PdfContentByte canvas = writer.getDirectContent();
			
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", true);
		    Font titleFont = new Font(bfChinese, 30, Font.BOLD);  
		    Font textFont = new Font(bfChinese, 18, Font.BOLD); 
		    Font valueFont = new Font(bfChinese , 12 , Font.NORMAL);
		    //-------------------  page1  -----------------------------------
			Paragraph title = new Paragraph("体检病例报告",  new Font(bfChinese, 50, Font.BOLD));
			title.setAlignment(1);
			Chapter chapter = new Chapter(new Paragraph("") , 1);
			
			chapter.setNumberDepth(0);
			chapter.add(title);
			PdfPTable firstTable = new PdfPTable(2);
			firstTable.setTotalWidth(280);
			firstTable.setSpacingBefore(80);
			firstTable.setHorizontalAlignment(1);
			
			firstTable.addCell(createCell("姓名 " , textFont , 0));
			firstTable.addCell(createCell(user.getName() , valueFont , 1));
			
			firstTable.addCell(createCell("性别 " , textFont , 0));
			firstTable.addCell(createCell(user.isMan()?"男" : "女" , valueFont , 1));
			
			firstTable.addCell(createCell("年龄 " , textFont , 0));
			firstTable.addCell(createCell(String.valueOf(user.getAge()) , valueFont , 1));
			
			firstTable.addCell(createCell("检测日期 " , textFont , 0));
			firstTable.addCell(createCell(new DateTime(user.getCreatedDate()).toString("yyyy-MM-dd") , 
									valueFont, 1 , 0));
			
			doc.add(chapter);
			firstTable.completeRow();
		    // write the table to an absolute position
			firstTable.writeSelectedRows(0, -1, 260, firstTable.getTotalHeight() + 80, canvas);
		    //-------------------  page1  -----------------------------------
			//-------------------  page2  -----------------------------------
			Paragraph userInfo = new Paragraph("个人信息",  titleFont);
			Chapter chapter2 = new Chapter(userInfo , 1);
			chapter2.setNumberDepth(0);
			
			PdfPTable infoTable = new PdfPTable(2);
			infoTable.setWidthPercentage(90);
			infoTable.setSpacingBefore(20);
			infoTable.setSpacingAfter(20);
			
			infoTable.addCell(createCell("姓名 " , textFont , 0));
			infoTable.addCell(createCell(user.getName() , valueFont , 1));
			
			infoTable.addCell(createCell("性别 " , textFont , 0));
			infoTable.addCell(createCell(user.isMan()?"男":"女" , valueFont , 1));

			infoTable.addCell(createCell("婚姻状况 " , textFont , 0));
			String married = "未婚";
			if (user.getMarried() == 1) {
				married = "已婚";
			} else if (user.getMarried() == 2) {
				married = "离异";
			}
			infoTable.addCell(createCell(married , valueFont , 1));
			
			infoTable.addCell(createCell("身高 " , textFont , 0));
			infoTable.addCell(createCell(StringUtils.valueOf(user.getStature()) + "CM" , valueFont , 1));
			
			infoTable.addCell(createCell("体重 " , textFont , 0));
			infoTable.addCell(createCell(StringUtils.valueOf(user.getWeight()) + "KG" , valueFont , 1));
			
			infoTable.addCell(createCell("生日 " , textFont , 0));
			infoTable.addCell(createCell(user.getBirthday()==null?"":new DateTime(user.getBirthday()
											).toString("yyyy-MM-dd"), valueFont , 1));
		
			infoTable.addCell(createCell("邮箱 " , textFont , 0 ));
			infoTable.addCell(createCell(user.getEmail(), valueFont , 1, 0));
			
			infoTable.addCell(createCell("身份证 " , textFont , 0));
			infoTable.addCell(createCell(user.getIdCard(), valueFont , 1 , 0));
			
			infoTable.addCell(createCell("电话 " , textFont , 0));
			infoTable.addCell(createCell(user.getMobile() , valueFont , 1));
			
			infoTable.addCell(createCell("现住址 " , textFont , 0));
			infoTable.addCell(createCell(user.getAddress() , valueFont , 1));
			
			infoTable.addCell(createCell("联系人1 " , textFont , 0));
			infoTable.addCell(createCell(StringUtils.valueOf(user.getEmContact1()), valueFont , 1));
			
			infoTable.addCell(createCell("联系人1 电话" , textFont , 0));
			infoTable.addCell(createCell(StringUtils.valueOf(user.getEmContact1Tel()), valueFont , 1));
			
			infoTable.addCell(createCell("联系人2 " , textFont , 0));
			infoTable.addCell(createCell(StringUtils.valueOf(user.getEmContact2()), valueFont , 1));
			
			infoTable.addCell(createCell("联系人2 电话" , textFont , 0));
			infoTable.addCell(createCell(StringUtils.valueOf(user.getEmContact2Tel()), valueFont , 1));
			
			infoTable.addCell(createCell("常住城市 " , textFont , 0));
			infoTable.addCell(createCell(StringUtils.valueOf(user.getCity()), valueFont , 1));
			
			infoTable.addCell(createCell("不良嗜好 " , textFont , 0));
			infoTable.addCell(createCell(user.getBadHabits() , valueFont , 1));
			
			infoTable.addCell(createCell("既往病史 " , textFont , 0));
			infoTable.addCell(createCell(user.getAnamnesis() , valueFont , 1));

			chapter2.add(infoTable);
			
			chapter2.add(new Paragraph("报告回复 ",  new Font(bfChinese, 18, Font.BOLD)));
			
			//获得所有回复
			List<HealthReply> replys = healthReplyService.findAllReplyByExamination(examination.getId());
			if (replys != null) {
				for (HealthReply reply : replys) {
					chapter2.add(new Paragraph(reply.getContent() ,  valueFont));
				}
			}
			doc.add(chapter2);
			//-------------------  page2  -----------------------------------
			//-------------------  page3  -----------------------------------
			Paragraph result = new Paragraph("各项检查结果如下: ",  titleFont);
			Chapter chapter3 = new Chapter(result , 1);
			chapter3.setNumberDepth(0);
			
			chapter3.add(new Paragraph("一般项目 ",  new Font(bfChinese, 18 , Font.BOLD)));
			
			PdfPTable resultTable1 = new PdfPTable(2);
			resultTable1.setSpacingBefore(20);
			resultTable1.setHorizontalAlignment(1);
			
			resultTable1.addCell(createCell("项目名称" , textFont , 1));
			resultTable1.addCell(createCell("项目结果" , textFont , 1));
			
			resultTable1.addCell(createCell("收缩压" , valueFont , 0));
			resultTable1.addCell(createCell(StringUtils.valueOf(examination.getBloodPressureLow()) , valueFont , 0));
			
			resultTable1.addCell(createCell("舒张压" , valueFont , 0));
			resultTable1.addCell(createCell(StringUtils.valueOf(examination.getBloodPressureHigh()) , valueFont , 0));
			
			resultTable1.addCell(createCell("体温" , valueFont , 0));
			resultTable1.addCell(createCell(StringUtils.valueOf(examination.getBodyTemp()) , valueFont , 0));
			
			chapter3.add(resultTable1);
			
			chapter3.add(new Paragraph("内科 ",  new Font(bfChinese, 18 , Font.BOLD)));
			
			PdfPTable resultTable2 = new PdfPTable(2);
			resultTable2.setSpacingBefore(20);
			resultTable2.setHorizontalAlignment(1);
			
			resultTable2.addCell(createCell("项目名称" , textFont , 1));
			resultTable2.addCell(createCell("项目结果" , textFont , 1));
			
			resultTable2.addCell(createCell("心率" , valueFont , 0));
			resultTable2.addCell(createCell(StringUtils.valueOf(examination.getHeartRhythm()) , valueFont , 0));

			resultTable2.addCell(createCell("血氧" , valueFont , 0));
			resultTable2.addCell(createCell(StringUtils.valueOf(examination.getBloodOxygen()) , valueFont , 0));

			chapter3.add(resultTable2);
			
			doc.add(chapter3);
			//-------------------  page3  -----------------------------------
			//-------------------  page4  -----------------------------------

			
			int w = 0;
			int h = 0;

			List<BufferedImage> sources = new ArrayList<BufferedImage>();
			for (int i = 1; i < 8; i++) {
//			//	bloodChapter.add(new Paragraph("心电图 " + i,  new Font(bfChinese, 12 , Font.BOLD)) );
				String ecgPath = String.valueOf(User.class.getSimpleName().toLowerCase() + "/" + user.getId()) + "/examination/" + examination.getId() + "/ecg" + i + ".jpg";
				BufferedImage source = ImageIO.read(new ByteArrayInputStream(uploadService.load(Type.heart_img , ecgPath)));
				if (source.getWidth() > w) {
					w = source.getWidth();
				}
				if (source.getHeight() > h) {
					h = source.getHeight();
				}
				sources.add(source);
			}

			Chapter ecgChapter;
			//int step = (int)(w / 7);
			int step = 1400;
			int j = 1;
			do {
				int x = step * j;
				if (x > w) {
					x = w;
				}
				if (j == 7) {
					break;
				}
				ecgChapter = new Chapter(new Paragraph("心电图 第" + j + "部分",  titleFont) , 1);
				ecgChapter.setNumberDepth(0);
				for (int i = 1; i < 8; i++) {
					BufferedImage source = sources.get(i - 1);
					BufferedImage dest = new BufferedImage(step , h , BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics = dest.createGraphics();
//				    AffineTransform trans = new AffineTransform();
//				    trans.rotate(Math.PI/2, h/2 , h/2);
//				    graphics.setTransform(trans);
				    graphics.drawImage(source , 0 , 0 , step , h , step*(j - 1) , 0 , x , h, null);
//				    graphics.dispose();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ImageIO.write(dest , "jpg", out);
					Image image = Image.getInstance(out.toByteArray());
					image.scalePercent(38, 19);
					ecgChapter.add(image);
				}
				doc.add(ecgChapter);
				j++;
			}
			while ((step * (j-1)) < w);

			/**
			 * 单独输出第8张图
			 */
			Chapter bloodChapter = new Chapter(new Paragraph("血氧图",  titleFont) , 1);
			String ecgPath = String.valueOf(User.class.getSimpleName().toLowerCase() + "/" + user.getId()) + "/examination/" + examination.getId() + "/ecg8.jpg";
			BufferedImage bSource = ImageIO.read(new ByteArrayInputStream(uploadService.load(Type.heart_img , ecgPath)));
			w = bSource.getWidth();
			j = 1;

			step = 1300;
			do {
				int x = step * j;
				if (x > w) {
					x = w;
				}
				if (j == 17) {
					break;
				}
				BufferedImage source = bSource;
				BufferedImage dest = new BufferedImage(step , h , BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = dest.createGraphics();
//			    AffineTransform trans = new AffineTransform();
//			    trans.rotate(Math.PI/2, h/2 , h/2);
//			    graphics.setTransform(trans);
			    graphics.drawImage(source , 0 , 0 , step , h , step*(j - 1) , 0 , x , h, null);
//			    graphics.dispose();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(dest , "jpg", out);
				Image image = Image.getInstance(out.toByteArray());
				image.scalePercent(34 , 17);
				bloodChapter.add(image);
				j++;
			}
			while ((step * (j-1)) < w);
			
			doc.add(bloodChapter);
			//-------------------  page4  -----------------------------------
			
			doc.close();
		}
		catch(Exception e) {
			throw new ServiceException(e , "exception.export.pdf.error");
		}
	}
	
	private PdfPCell createCell(String name , Font font , float borderBottom) {
		PdfPCell cell = new PdfPCell(new Phrase(name , font));
		cell.setBorderWidth(0);
		cell.setHorizontalAlignment(1);
		cell.setBorderWidthBottom(borderBottom);
		return cell;
	}
	
	private PdfPCell createCell(String name , Font font , float borderBottom , int align) {
		PdfPCell cell = new PdfPCell(new Phrase(name , font));
		cell.setBorderWidth(0);
		cell.setHorizontalAlignment(align);
		cell.setBorderWidthBottom(borderBottom);
		return cell;
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}


}
