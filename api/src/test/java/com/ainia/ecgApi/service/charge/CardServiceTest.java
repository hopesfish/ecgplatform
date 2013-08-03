package com.ainia.ecgApi.service.charge;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import au.com.bytecode.opencsv.CSVReader;

import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.utils.PropertyUtil;
import com.ainia.ecgApi.domain.charge.Card;

/**
 * <p>
 * Card Service test
 * </p>
 * Copyright: Copyright (c) 2013 Company: CardServiceTest.java
 * 
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
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
		card.setExpireDate(new DateTime(2015 , 1, 1, 0 ,0 ,0).toDate());
		card.setCreatedBatch(1);
	}

	@Test
	public void testFind() {
		Query<Card> query = new Query<Card>();
		List<Card> cards = cardService.findAll(query);

		// Assert.assertNotEquals(cards.size() , 0);
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
	public void testEncodedSerial() {
		String[] serials = new String[] { "888888", "0032153088270012",
				"0033178902311110", "0067890200312293", "1000", "1001", "1002",
				"1003", "1004", "1005", "1006", "1007", "1008", "1009", "1010" };

		for (String serial : serials) {
			System.out.println("=========================");
			System.out.println("serial " + serial);
			System.out.println("encodedSerial "
					+ cardService.encodeString(serial, null));
		}
	}

	@Test
	public void testUpload() throws Exception {
		CSVReader reader = new CSVReader(new InputStreamReader(Thread
				.currentThread().getContextClassLoader()
				.getResourceAsStream("card/upload.csv")));
		List<String[]> list = reader.readAll();
		List<Card> cards = new ArrayList(list.size());
		SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
		for (String[] values : list) {
			Card card = new Card();
			PropertyUtil.setProperty(card, Card.ENCODED_SERIAL, values[0]);
			PropertyUtil.setProperty(card, Card.ENCODED_PASSWORD, values[1]);
			PropertyUtil.setProperty(card, Card.DAYS, values[2]);
			PropertyUtil.setProperty(card, Card.EXPIRED_DATE, format.parse(values[3]));

			cards.add(card);
		}
		cardService.create(cards);
	}
	
	@Test
	public void testUploadError() throws Exception {
		CSVReader reader = new CSVReader(new InputStreamReader(Thread
				.currentThread().getContextClassLoader()
				.getResourceAsStream("card/upload-error.csv")));
		List<String[]> list = reader.readAll();
		long count = cardService.count(new Query());
		try {
			cardService.createByUpload(list);
		}catch(Exception e) {
			
		}
		Assert.assertTrue(count == cardService.count(new Query()));
	}
}
