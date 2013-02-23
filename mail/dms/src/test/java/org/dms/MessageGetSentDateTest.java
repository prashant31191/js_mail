package org.dms;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageGetSentDateTest {
	private static Message message = null;
	
	@Before
	public void beforeTest() {
		message = new Message();
	}
	
	@Test
	public void getIdTest() {
		Date date = new Date(System.currentTimeMillis());
		message.setSentDate(date);
		Assert.assertEquals(date, message.getSentDate());
	}
}
