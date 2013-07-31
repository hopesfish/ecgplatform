package com.ainia.ecgApi.service.charge;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.domain.charge.Card;

/**
 * <p>Card Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * CardServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    private static Card card;
    
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }
    
    
    
    @Before
    public void setUp() {
        card = new Card();
        card.setEncodedSerial("1111111111111111");
        card.setSerial("11111111111111111111111");
        card.setEncodedPassword("111111");
        card.setDays(1);
        card.setCreatedBatch(1);
    }
    
    @Test
    public void testFind() {
        Query<Card> query = new Query<Card>();
        List<Card> cards = cardService.findAll(query);
        
        //Assert.assertNotEquals(cards.size() , 0);
    }

    @Test
    public void testCreate() {
        cardService.create(card);
        
        Assert.assertTrue(card.getId() != null);
        
        cardService.delete(card);
    }
    
    @Test
    public void testUpdate() {
        cardService.create(card);
        Card _card = cardService.update(card);
        
        cardService.delete(_card);
    }
    
    @Test
    public void testPatch() {
        cardService.create(card);
        
        Card _card = cardService.update(card);
        
        cardService.delete(_card);
    }
    
    @Test
    public void encodedSerial() {
    	String[] serials = new String[]{"0032153088270012" , "0033178902311110","0067890200312293"};
    	
    	for (String serial : serials) {
        	System.out.println("=========================");
        	System.out.println("serial " + serial);
        	System.out.println("encodedSerial " + cardService.encodeString(serial , null));
        	System.out.println("=========================");
    	}
    }
    
    
}
