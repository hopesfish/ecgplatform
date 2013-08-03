package com.ainia.ecgApi.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.exception.PermissionException;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.utils.DigestUtils;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.core.utils.PropertyUtil;
import com.ainia.ecgApi.dao.sys.UserDao;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.dto.common.Message;
import com.ainia.ecgApi.dto.common.Message.Type;
import com.ainia.ecgApi.service.common.MessageService;

/**
 * <p>User Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UserServiceImpl.java
 * @author pq
 * @createdDate 2013-06-22
 * @version 0.1
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User , Long> implements UserService {
	
	public static final int RETAKE_CODE_NUM = 3;
	public static final int RETAKE_MAX_NUM = 3;
    
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private MessageService messageService;
    
    public BaseDao<User , Long> getBaseDao() {
        return userDao;
    }
    

	@Override
	public User create(User user) {
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword(authenticateService.encodePassword(user.getPassword() , null));
		}
		return super.create(user);
	}
	
	private boolean hasPermission(User user) {
		AuthUser currentUser = authenticateService.getCurrentUser();
		if (currentUser == null || (!currentUser.isSuperAdmin() && !user.getId().equals(currentUser.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public User update(User user) {
		if (!hasPermission(user)) {
			throw new PermissionException("exception.user.cannotChange");
		}
		//not allow change the password
		User old =  userDao.findOne(user.getId());
		List<String> excludes = new ArrayList<String>(1);
		excludes.add(Employee.PASSWORD);
		PropertyUtil.copyProperties(old , user , excludes);
		return userDao.save(old);
	}

	@Override
	public User patch(User user) {
		if (!hasPermission(user)) {
			throw new PermissionException("exception.user.cannotChange");
		}
		//not allow change the password
		User old = userDao.findOne(user.getId());
		user.setPassword(old.getPassword());
		return userDao.save(user);
	}

	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	public void changePassword(Long id, String oldPassword,
			String newPassword) {
		User user = this.get(id);
		if (user == null) {
			throw new ServiceException("exception.notFound");
		}
		if (!hasPermission(user)) {
			throw new PermissionException("exception.password.cannotChange");
		}
		
		if (!authenticateService.checkPassword(user.getPassword() , oldPassword , null)) {
			throw new ServiceException("exception.oldPassword.notEquals");
		}
		user.setPassword(authenticateService.encodePassword(newPassword , null));
		this.userDao.save(user);
	}
	
	public void resetPassword(Long id) {
		User user = this.get(id);
		if (user == null) {
			throw new ServiceException("exception.notFound");
		}
		if (!hasPermission(user)) {
			throw new ServiceException("exception.password.cannotChange");
		}
		user.setPassword(authenticateService.encodePassword(user.getUsername() , null));
		this.userDao.save(user);
		
	}


	@Override
	public void delete(Long id) {
		User user = this.get(id);
		AuthUser currentUser = authenticateService.getCurrentUser();
		if (currentUser.getId().equals(id)) {
			throw new ServiceException("exception.user.cannotDeleteSelf");
		}
		if (!hasPermission(user)) {
			throw new ServiceException("exception.user.cannotDelete");
		}
		super.delete(id);
	}


	@Override
	public void delete(User user) {
		AuthUser currentUser = authenticateService.getCurrentUser();
		if (currentUser.getId().equals(user.getId())) {
			throw new ServiceException("exception.user.cannotDeleteSelf");
		}
		if (!hasPermission(user)) {
			throw new ServiceException("exception.user.cannotDelete");
		}
		super.delete(user);
	}


	@Override
	public void retakePassword(User user , Type messageType) {
		//生成随机6为找回码
		String code = EncodeUtils.encodeHex(DigestUtils.generateSalt(RETAKE_CODE_NUM));
		user.setRetakeCode(code);
		user.setRetakeDate(new Date());
		userDao.save(user);
		//TODO 短信 邮件接口修改后 同步修改发送内容
		switch(messageType) {
		case email:
			messageService.sendEmail(new Message("" , code , null , user.getEmail() , null));
			break;
		case sms:
			messageService.sendSms(new Message("" , code , null , "13027334591" , null));
			break;
		default:
			throw new ServiceException("exception.message.unknown");
		}
 	}


	@Override
	public void retakePassword(User user, String code, String newPassword) {
		String retakeCode = user.getRetakeCode();
		Date   retakeDate = user.getRetakeDate();
		Integer retakeCount= user.getRetakeCount() == null ? 1 : user.getRetakeCount();
		if (StringUtils.isBlank(newPassword) || StringUtils.isBlank(code)) {
			throw new ServiceException("exception.user.retakePassword.infoError");
		}
		if (new DateTime(retakeDate).plusMinutes(30).isBefore(new Date().getTime())) {
			throw new ServiceException("exception.user.retakePassword.dateTimeout");
		}
		if (!retakeCode.equals(code)) {
			if (retakeCount > RETAKE_MAX_NUM) {
				user.setRetakeCode(null);
				user.setRetakeCount(null);
				user.setRetakeDate(null);
			}
			else {
				user.setRetakeCount(retakeCount + 1);
			}
			userDao.save(user);
			throw new ServiceException("exception.user.retakePassword.codeNotEqual");
		}
		//reset the password
		user.setPassword(authenticateService.encodePassword(newPassword , null));
		userDao.save(user);
	}


	@Override
	public User findByMobile(String mobile) {
		return userDao.findByMobile(mobile);
	}


	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	} 
	
	

}
