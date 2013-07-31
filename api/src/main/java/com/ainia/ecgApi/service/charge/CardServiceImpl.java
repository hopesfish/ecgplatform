package com.ainia.ecgApi.service.charge;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.utils.DigestUtils;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.dao.charge.CardDao;
import com.ainia.ecgApi.domain.charge.Card;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.sys.EmployeeService;

/**
 * <p>Card Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * CardServiceImpl.java
 * @author pq
 * @createdDate 2013-07-29
 * @version 0.1
 */
@Service
public class CardServiceImpl extends BaseServiceImpl<Card , Long> implements CardService {
    
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;

	private static final int SALT_SIZE = 8;
	
    @Autowired
    private CardDao cardDao;
    @Autowired
    private EmployeeService employeeService;
    
    @Override
    public BaseDao<Card , Long> getBaseDao() {
        return cardDao;
    }

	@Override
	public void charge(String serial, String password ,  Date activedDate, Long employeeId , User user) {
		Card card = cardDao.findByEncodedSerial(this.encodePassword(serial , null));
		if (card == null) {
			throw new ServiceException("exception.card.notFound");
		}
		//卡号密码判断
		if (!this.checkPassword(card.getEncodedPassword() , password , null)) {
			throw new ServiceException("exception.card.errorPassword");
		}
		if (activedDate == null || activedDate.before(new Date())) {
			throw new ServiceException("exception.card.invalidActiveDate");
		}
		if (employeeId != null) {
			Employee employee = employeeService.get(employeeId);
			if (employee != null) {
				card.setEmployeeId(employeeId);
				card.setEmployeeName(employee.getName());
			}
		}
		card.setSerial(serial);
		card.setUserId(user.getId());
		card.setUserName(user.getName());
		card.setActivedDate(activedDate);
		
		this.update(card);
	}

	public String encodePassword(String password , byte[] salt) {
		byte[] hashPassword = DigestUtils.sha1(password.getBytes() , salt, HASH_INTERATIONS);
		return EncodeUtils.encodeHex(hashPassword);
	}
	
	public boolean checkPassword(String target, String source , byte[] salt) {
		String hashPassword = this.encodePassword(source , null);
		if (log.isDebugEnabled()) {
			log.debug(String.format("compare the password %s %s" , hashPassword , target));
		}
		return hashPassword.equals(target);
	}

	@Override
	public Card findBySerial(String serial) {
		return cardDao.findBySerial(serial);
	}
}
