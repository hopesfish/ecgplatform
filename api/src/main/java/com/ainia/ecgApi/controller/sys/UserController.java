package com.ainia.ecgApi.controller.sys;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.crud.Page;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.domain.health.HealthRule;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.domain.task.Task;
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
    public ResponseEntity<Set<HealthRule>> getRules(@PathVariable("id") Long id) {
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
	 * @param id
	 * @param query
	 * @return
	 * ResponseEntity<Page<Task>>
	 */
	@RequestMapping(value = "{id}/task" , method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Page<Task>> findTask(@PathVariable("id") Long id , Query<Task> query) {
		query.eq(Task.USER_ID  , id);
		query.addOrder(Task.CREATED_DATE , OrderType.desc);
		long total = taskService.count(query);
		query.getPage().setTotal(total);
		query.getPage().setDatas(taskService.findAll(query));
		return new ResponseEntity(query.getPage() ,HttpStatus.OK);
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
	public ResponseEntity changePassword(@PathVariable("id") Long id , 
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
}
