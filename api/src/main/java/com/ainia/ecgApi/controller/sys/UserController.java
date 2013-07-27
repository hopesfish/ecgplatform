package com.ainia.ecgApi.controller.sys;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
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
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthRule;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.domain.task.Task;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;
import com.ainia.ecgApi.service.health.HealthExaminationService;
import com.ainia.ecgApi.service.health.HealthRuleService;
import com.ainia.ecgApi.service.sys.UserService;
import com.ainia.ecgApi.service.task.TaskService;

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
		if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
			userService.resetPassword(id);
		}
		else {
			userService.changePassword(id, oldPassword, newPassword);
		}
		return new ResponseEntity(HttpStatus.OK);
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
	@RequestMapping(value = "{id}/examination" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResponseEntity upload(@PathVariable("id") Long id , @RequestParam("file") MultipartFile file , HealthExamination examination ,
    								@RequestParam("md5") String md5) throws IOException {
		
		ResponseEntity entity = new ResponseEntity(HttpStatus.OK);
    	if (file.getBytes().length == 0) {
    		entity =  new ResponseEntity(HttpStatus.BAD_REQUEST);
    	}
		AuthUser authUser = authenticateService.getCurrentUser();	
		if (authUser == null) {
			entity = new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		else if (!User.class.getSimpleName().equals(authUser.getType()) || !authUser.getId().equals(id)) {
			entity = new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		else {
			healthExaminationService.upload(examination , file.getBytes() , md5);
		}
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
		HealthExamination examination = healthExaminationService.get(examinationId);
		//TODO 文件后缀名固定
		String ecgPath = String.valueOf(User.class.getSimpleName().toLowerCase() + "/" + examination.getUserId()) + "/examination/" + examination.getId() + "/ecg" + index + ".jpg";
		try {
			response.setContentType("image/jpeg");
			response.getOutputStream().write(uploadService.load(Type.heart_img , ecgPath));
			response.getOutputStream().flush();
		} catch (IOException e) {
			throw new ServiceException("examination.ecgPath.notFound");
		}
	}
}
