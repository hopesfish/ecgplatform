package com.ainia.ecgApi.controller.charge;

import java.util.Date;

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

import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.domain.charge.Card;
import com.ainia.ecgApi.service.charge.CardService;
import com.ainia.ecgApi.service.sys.UserService;

/**
 * <p>Card controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * CardController.java
 * @author pq
 * @createdDate 2013-07-29
 * @version 0.1
 */
@Controller
@RequestMapping("/api/card")
public class CardController  {

    @Autowired
    private CardService cardService;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private UserService userService;
    /**
     * <p>已充值卡列表</p>
     * @param query
     * @return
     * ResponseEntity
     */
    @RequestMapping(method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity index(Query query) {
    	query.isNotNull(Card.ACTIVED_DATE);
    	query.isNotNull(Card.USER_ID);
    	query.addOrder(Card.ACTIVED_DATE , OrderType.desc);
		long total = cardService.count(query);
		query.getPage().setTotal(total);
		query.getPage().setDatas(cardService.findAll(query));
		
		return new ResponseEntity(query.getPage() , HttpStatus.OK);
    }
    
    /**
     * <p>查询指定已激活卡号</p>
     * @param serial
     * @return
     * ResponseEntity
     */
    @RequestMapping(value = "{serial}" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable("serial") String serial) {
    	Card card = cardService.findBySerial(serial);
    	AuthUser authUser = authenticateService.getCurrentUser();
    	if (card == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	if (authUser.isUser() &&  !authUser.getId().equals(card.getUserId())) {
    		return new ResponseEntity(HttpStatus.FORBIDDEN);
    	}
    	return new ResponseEntity(card , HttpStatus.OK);
    }
    /**
     * <p>卡号重置</p>
     * @param serial
     * @param password
     * @param activedDate
     * @param userId
     * @return
     * ResponseEntity
     */
    @RequestMapping(value = "{serial}/charge" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity charge(@PathVariable("serial") String serial , @RequestParam String password,
    								@RequestParam("activedDate") Date activedDate , @RequestParam("userId") Long userId) {
    	AuthUser authUser = authenticateService.getCurrentUser();
    	if (!authUser.isEmployee()) {
    		return new ResponseEntity(HttpStatus.FORBIDDEN);
    	}
    	cardService.charge(serial, password ,  activedDate , authUser.getId() , userService.get(userId));
    	
    	return new ResponseEntity(HttpStatus.OK);
    }

}
