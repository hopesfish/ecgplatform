package com.ainia.ecgApi.controller.sys;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.crud.Page;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.charge.Card;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthRule;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.domain.task.Task;
import com.ainia.ecgApi.dto.common.Message;
import com.ainia.ecgApi.service.charge.CardService;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;
import com.ainia.ecgApi.service.health.HealthExaminationService;
import com.ainia.ecgApi.service.health.HealthRuleService;
import com.ainia.ecgApi.service.sys.UserService;
import com.ainia.ecgApi.service.task.TaskService;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * <p>User controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UserController.java
 * @author pq
 * @createdDate 2013-06-22
 * @version 0.1
 */
@Controller
@RequestMapping("/api/user")
public class UserController extends BaseController<User , Long> {

    @Autowired
    private UserService userService;
    @Autowired
    private HealthRuleService healthRuleService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private HealthExaminationService healthExaminationService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private CardService cardService;
    
    @Override
    public BaseService<User , Long> getBaseService() {
        return userService;
    }

    /**
     * <p>获取用户至规则</p>
     * @param id
     * @return
     * ResponseEntity
     */
    @RequestMapping(value = "{id}/rule" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Set<HealthRule>> findRules(@PathVariable("id") Long id) {
    	User user = userService.get(id);
    	if (user == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	return new ResponseEntity(user.getRules()  , HttpStatus.OK);
    }
    
    /**
     * <p>将规则绑定至用户</p>
     * @param id
     * @return
     * ResponseEntity
     */
    @RequestMapping(value = "{id}/rule/{ruleId}" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity addRule(@PathVariable("id") Long id , @PathVariable("Id") Long ruleId) {
    	User user = userService.get(id);
    	HealthRule healthRule = healthRuleService.get(id);
    	if (healthRule == null || user == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	healthRuleService.addUser(ruleId , id);
    	return new ResponseEntity(HttpStatus.CREATED);
    }
    
	/**
	 * <p>获取用户相关任务</p>
	 * @param idzvdzcx
	 * @param query
	 * @return
	 * ResponseEntity<Page<Task>>
	 */
	@RequestMapping(value = "{id}/task" , method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Page<Task>> findTask(@PathVariable("id") Long id , Query<Task> query) {
		AuthUser currentUser = authenticateService.getCurrentUser();
		if (currentUser!= null && currentUser.isUser() && !currentUser.getId().equals(id)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		query.eq(Task.USER_ID  , id);
		query.addOrder(Task.CREATED_DATE , OrderType.desc);
		long total = taskService.count(query);
		query.getPage().setTotal(total);
		query.getPage().setDatas(taskService.findAll(query));
		return new ResponseEntity(query.getPage() ,HttpStatus.OK);
	}  
	
	/**
	 * <p>删除用户相关任务</p>
	 * @param idzvdzcx
	 * @param query
	 * @return
	 * ResponseEntity<Page<Task>>
	 */
	@RequestMapping(value = "{id}/task" , method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity deleteAllTask(@PathVariable("id") Long id) {
		User user = userService.get(id);
		if (user == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		taskService.deleteAllByUser(id);
		return new ResponseEntity(HttpStatus.OK);
	}  
	
	/**
	 * <p>删除用户单个任务</p>
	 * @param idzvdzcx
	 * @param query
	 * @return
	 * ResponseEntity<Page<Task>>
	 */
	@RequestMapping(value = "{id}/task/{taskId}" , method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity deleteTask(@PathVariable("taskId") Long taskId) {
		Task task = taskService.get(taskId);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		taskService.delete(taskId);
		return new ResponseEntity(HttpStatus.OK);
	}  
    
    /**
     * <p>将规则与用户解除</p>
     * @param id
     * @return
     * ResponseEntity
     */
    @RequestMapping(value = "{id}/user/{ruleId}" , method = RequestMethod.DELETE , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity removeRule(@PathVariable("id") Long id , @PathVariable("ruleId") Long ruleId) {
    	User user = userService.get(id);
    	HealthRule healthRule = healthRuleService.get(ruleId);
    	if (user == null || healthRule == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	healthRuleService.removeUser(ruleId ,  id);
    	return new ResponseEntity(HttpStatus.CREATED);
    }
    
    
    
	/**
	 * <p>修改密码</p>
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "{id}/password" ,method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<?> changePassword(@PathVariable("id") Long id , 
										 @RequestParam(value = "oldPassword" , required = false) String oldPassword ,
										 @RequestParam(value = "newPassword" , required = false) String newPassword) {
		User user = userService.get(id);
		if (user == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
			userService.resetPassword(id);
		}
		else {
			userService.changePassword(id, oldPassword, newPassword);
		}
		user.setSalt(EncodeUtils.encodeHex(EncodeUtils.asciiSum(user.getPassword()).getBytes()));
		userService.update(user);
		
		Map result = new HashMap(1);
		result.put(AjaxResult.AUTH_TOKEN , authenticateService.generateToken(user.getUsername() , User.class.getSimpleName(),
											user.getTokenDate() ,
											user.getSalt()));
		return new ResponseEntity(result , HttpStatus.OK);
	}
	
    /**
     * <p>获取用户相关健康测试</p>
     * @param id
     * @return
     * ResponseEntity<List<HealthExamination>>
     */
    @RequestMapping(value = "{id}/examination" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity findExaminations(@PathVariable("id") Long id , Query<HealthExamination> query) {
    	User user = userService.get(id);
    	if (user == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	query.eq(HealthExamination.USER_ID , id);
    	query.addOrder(HealthExamination.CREATED_DATE , OrderType.desc);
    	long total = healthExaminationService.count(query);
		query.getPage().setTotal(total);
		query.getPage().setDatas(healthExaminationService.findAll(query));
    	return new ResponseEntity(query.getPage() , HttpStatus.OK);
    }
	
    /**
     * <p>健康测试数据上传接口</p>
     * @param file
     * @return
     * ResponseEntity
     * @throws IOException 
     */
	@RequestMapping(value = "{uploadUserId}/examination" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResponseEntity upload(@PathVariable("uploadUserId") Long uploadUserId , @RequestParam(value = "file" , required = false) MultipartFile file , HealthExamination examination ,
    								@RequestParam(value = "md5" , required = false) String md5) throws IOException {
		
		ResponseEntity entity = new ResponseEntity(HttpStatus.OK);
    	// 必须登录, 必须是用户类型, 必须是本人提交
		AuthUser authUser = authenticateService.getCurrentUser();	
		if (authUser == null) {
			entity = new ResponseEntity(HttpStatus.FORBIDDEN);
			return entity;
		} else if (!User.class.getSimpleName().equals(authUser.getType()) || !authUser.getId().equals(uploadUserId)) {
			log.error("the api should only invoked by the user him/herself.");
			entity = new ResponseEntity(HttpStatus.FORBIDDEN);
			return entity;
		}

		// 非测试情况文件不能为空
		if (file == null || file.getBytes().length == 0) {
			if (examination.getIsTest() == null || !examination.getIsTest()) {
				log.error("the file should not be null.");
				entity = new ResponseEntity(HttpStatus.BAD_REQUEST);
				return entity;
			}
		}

		// apkId不能为空
		if (examination.getApkId() == null) {
			log.error("the apkId should not be null.");
			entity = new ResponseEntity(HttpStatus.BAD_REQUEST);
			return entity;
		}
		
		healthExaminationService.upload(examination , file.getBytes() , md5);

    	return entity;
    }
	
	/**
	 * <p>获取心电图1</p>
	 * @return
	 * byte[]
	 * @throws IOException 
	 */
	@RequestMapping(value = "{id}/examination/{examinationId}/ecg{index}" , method = RequestMethod.GET)
	public void loadEcg(@PathVariable("id") Long id , @PathVariable("examinationId") Long examinationId , 
										@PathVariable("index") int index, HttpServletResponse response)  {
		//TODO 文件后缀名固定
		String ecgPath = String.valueOf(User.class.getSimpleName().toLowerCase() + "/" +id) + "/examination/" + examinationId + "/ecg" + index + ".jpg";
		try {
			response.setContentType("image/jpeg");
			response.getOutputStream().write(uploadService.load(Type.heart_img , ecgPath));
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("examination.ecgPath.notFound");
		}
	}
	
	@RequestMapping(value = "{id}/examination/{examinationId}/pdf" , method = RequestMethod.GET) 
	public void loadExaminationByPdf(@PathVariable("examinationId") Long examinationId
										, HttpServletResponse response) throws DocumentException, IOException {
		HealthExamination examination = healthExaminationService.get(examinationId);
		if (examination == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		User user = userService.get(examination.getUserId());
		
		StringBuffer fileName = new StringBuffer();
		fileName.append("体检测试报告-").append(user.getName())
				.append("-").append(new DateTime().toString("yyyy-MM-dd"));
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName.toString() ,"UTF-8"));
		healthExaminationService.exportPDF(examination,  response.getOutputStream());
		
	}
	
	/**
	 * <p>用户充值</p>
	 * @param id
	 * @param serial
	 * @param password
	 * @param activedDate
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "{id}/charge" , method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity charge(@PathVariable("id") Long id ,@RequestParam("serial") String serial ,  @RequestParam String password , 
				@RequestParam("activedDate") Date activedDate) {
    	AuthUser authUser = authenticateService.getCurrentUser();
    	if (!authUser.isUser()) {
    		return new ResponseEntity(HttpStatus.FORBIDDEN);
    	}
    	cardService.charge(serial, password ,  activedDate , null , userService.get(authUser.getId()));
    	
    	return new ResponseEntity(HttpStatus.OK);
	}
	/**
	 * <p>获得用户的卡</p>
	 * @param id
	 * @param query
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "{id}/card" , method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity findCards(@PathVariable("id") Long id , Query query) {
    	AuthUser authUser = authenticateService.getCurrentUser();
    	query.isNotNull(Card.ACTIVED_DATE);
    	query.isNotNull(Card.USER_ID);
    	query.eq(Card.USER_ID , authUser.getId());
    	query.addOrder(Card.ACTIVED_DATE , OrderType.desc);
		long total = cardService.count(query);
		query.getPage().setTotal(total);
		query.getPage().setDatas(cardService.findAll(query));
		
		return new ResponseEntity(query.getPage() , HttpStatus.OK);
	}
	
	/**
	 * <p>重置密码请求</p>
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "password/retake" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity retakePasswordRequest(@RequestParam(value = "mobile" , required = false) String mobile , 
								@RequestParam(value = "email" , required = false) String email) {
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)) {
			response = new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isNotBlank(email)) {
			User user = userService.findByEmail(email);
			if (user == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			userService.retakePassword(user, Message.Type.email);
			response = new ResponseEntity(HttpStatus.OK);
		}
		if (StringUtils.isNotBlank(mobile)){
			User user = userService.findByMobile(mobile);
			if (user == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			userService.retakePassword(user, Message.Type.sms);
		}
		return response;
	}
	/**
	 * <p>通过随机码重置密码</p>
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "password/retake" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity retakePasswordByCode(@RequestParam(value = "mobile" , required = false) String mobile , 
						@RequestParam(value = "email" , required = false) String email , 
						@RequestParam("code") String code, @RequestParam("newPassword") String newPassword) {
		ResponseEntity response;
		if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)) {
			response = new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(newPassword) || StringUtils.isBlank(code)) {
			response = new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		if (StringUtils.isNotBlank(email)) {
			User user = userService.findByEmail(email);
			if (user == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			userService.retakePassword(user, code , newPassword);
			response = new ResponseEntity(HttpStatus.OK);
		}
		else {
			User user = userService.findByMobile(mobile);
			if (user == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			userService.retakePassword(user, code , newPassword);
			response = new ResponseEntity(HttpStatus.OK);
		}
		return response;
		
	}
}
