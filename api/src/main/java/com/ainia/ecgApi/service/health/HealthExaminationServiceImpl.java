package com.ainia.ecgApi.service.health;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.dao.health.HealthExaminationDao;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.domain.health.HealthRule;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.ainia.ecgApi.domain.health.HealthRuleReply;
import com.ainia.ecgApi.domain.sys.SystemConfig;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.domain.task.Task.Status;
import com.ainia.ecgApi.dto.health.HealthInfo;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;
import com.ainia.ecgApi.service.sys.SystemConfigService;
import com.ainia.ecgApi.service.task.ExaminationTaskService;
import com.ainia.ecgApi.service.task.TaskService;
import com.ainia.ecgApi.utils.DataProcessor;
import com.ainia.ecgApi.utils.ECGChart;
import com.ainia.ecgApi.utils.OxygenChart;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
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
    
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    
    
    @Override
    public BaseDao<HealthExamination , Long> getBaseDao() {
        return healthExaminationDao;
    }

	public void reply(Long id , HealthReply reply) {
		healthReplyService.create(reply);
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
				
		    	Query<HealthRuleReply> repliyConfgQuery = new Query();
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

	public void upload(final HealthExamination examination , final byte[] uploadData , String md5) {

		// 判断是否有效登录
		final AuthUser authUser = authenticateService.getCurrentUser();	
		if (authUser == null) {
			throw new ServiceException("authUser.error.notFound");
		}
		if (!User.class.getSimpleName().equals(authUser.getType())) {
			throw new ServiceException("examination.error.upload.notAllowed");
		}

		// 判断是否是测试请求
		boolean isTest = examination.getIsTest() == null ? false : examination.getIsTest();
		if (uploadData == null && !isTest) {
			throw new ServiceException("file.is.empty");
		} else if (uploadData != null && uploadData.length == 0) {
			throw new ServiceException("file.length.is.zero");
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
						//save the file
				    	DataProcessor processor = new DataProcessor();
				    	processor.process(uploadData , uploadData.length);
						float[] daolian = processor.getDaolian_i();
						
						//生成文件相对路径
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
						String oxyUri = uploadService.save(Type.heart_img,
								oxyPath, oxyChart);
						
						//存储原始文件
						String rawPath = "user/" +  String.valueOf(authUser.getId()) + "/examination/" + examination.getId() + "/raw";
						String rawUri = uploadService.save(Type.heart_img , rawPath , uploadData);
						
						examination.setHeartData(rawUri);
						
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
		List<Map> results = new ArrayList(list.size());
		try {
			//TODO 暂时使用日期方式转换 处理日期格式化
			SimpleDateFormat from = new SimpleDateFormat("yyyy-M-d");
			SimpleDateFormat to   = new SimpleDateFormat("yyyy-MM-dd");
			for (Object[] data : list) {
				Map dataMap = new HashMap();
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
			e.printStackTrace();
			throw new ServiceException("exception.examination.statisticsError");
		}
		return results;
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}
}
