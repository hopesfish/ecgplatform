package com.ainia.ecgApi.service.charge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.utils.DigestUtils;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.core.utils.PropertyUtil;
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

    private boolean isOnService(User user, Card usingCard, Date activedDate) {
    	boolean flag = false;
    	int outer = 0;
    	
    	Query query = new Query();
    	query.isNotNull(Card.ACTIVED_DATE);
    	query.isNotNull(Card.USER_ID);
    	query.eq(Card.USER_ID , user.getId());
    	query.addOrder(Card.ACTIVED_DATE , OrderType.desc);
    	
		List <Card> cards = cardDao.findAll(query);
		if (cards != null) {
			GregorianCalendar gc = new GregorianCalendar();
			
			Date newEndDate;
			gc.setTime(activedDate);
			gc.add(GregorianCalendar.DATE, usingCard.getDays());
			newEndDate = gc.getTime();
			
			for (Card card: cards) {
				Date endDate;
				gc.setTime(card.getActivedDate());
				gc.add(GregorianCalendar.DATE, card.getDays());
				endDate = gc.getTime();
				if (card.getActivedDate().after(newEndDate) || endDate.before(activedDate)) {
					outer++;
				}
			}
			if (outer != cards.size()) {
				flag = true;
			}
		}
    	return flag;
    }

	@Override
	public void charge(String serial, String password, Date activedDate, Long employeeId , User user) {
		Card card = cardDao.findByEncodedSerial(this.encodeString(serial , null));
		// 卡号是否正确
		if (card == null) {
			throw new ServiceException("exception.card.notFound");
		}
		// 是否已经激活
		if (card.getActivedDate() != null) {
			throw new ServiceException("exception.card.used");
		}
		// 是否在可用时间
		if (card.getExpireDate().before(new Date())) {
			throw new ServiceException("exception.card.expired");
		}
		// 激活时间是否正确
		if (activedDate == null || activedDate.before(new Date())) {
			throw new ServiceException("exception.card.invalidActiveDate");
		}
		//卡号密码判断
		if (!this.checkString(card.getEncodedPassword() , password , null)) {
			throw new ServiceException("exception.card.errorPassword");
		}
		// 判断是否有操作员工
		if (employeeId != null) {
			Employee employee = employeeService.get(employeeId);
			if (employee != null) {
				card.setEmployeeId(employeeId);
				card.setEmployeeName(employee.getName());
			} else {
				throw new ServiceException("exception.card.employee.not.found");
			}
		}
		// 判断激活时间端是否已经充值
		if (isOnService(user, card, activedDate)) {
			throw new ServiceException("exception.card.user.still.in.service.time");
		}
		card.setSerial(serial);
		card.setUserId(user.getId());
		card.setUserName(user.getName());
		card.setChargedDate(new Date());
		card.setActivedDate(activedDate);
		
		this.update(card);
	}

	public String encodeString(String string , byte[] salt) {
		byte[] hashString = DigestUtils.sha1(string.getBytes() , salt, HASH_INTERATIONS);
		return EncodeUtils.encodeHex(hashString);
	}
	
	public boolean checkString(String target, String source , byte[] salt) {
		String hashString = this.encodeString(source , null);
		if (log.isDebugEnabled()) {
			log.debug(String.format("compare the password %s %s" , hashString , target));
		}
		return hashString.equals(target);
	}

	@Override
	public Card findBySerial(String serial) {
		Card card = cardDao.findByEncodedSerial(this.encodeString(serial, null));
		// 卡号未使用之前是不存的
		card.setSerial(serial);
		return card;
	}

	@Override
	public void createByUpload(List<String[]> list) {
        SimpleDateFormat format   = new SimpleDateFormat("yyyy-MM-dd");
        List<String> errors = new ArrayList();
        for (String[] values : list) {
        	try {
	        	Card card = new Card();
	        	PropertyUtil.setProperty(card , Card.ENCODED_SERIAL , this.encodeString(values[0] , null));
	        	PropertyUtil.setProperty(card , Card.ENCODED_PASSWORD , this.encodeString(values[1], null));
	        	PropertyUtil.setProperty(card , Card.DAYS , values[2]);
	        	PropertyUtil.setProperty(card , Card.EXPIRED_DATE , format.parse(values[3]));
	        	this.create(card);
        	}catch(RuntimeException r) {
        		r.printStackTrace();
        		errors.add(values[0]);
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        		errors.add(values[0]);
        	}
        }
        if (errors.size() > 0) {
        	throw new ServiceException("exception.card.errorCard" , errors);
        }
	}
}
