package org.cc;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageGetDateTest {
private static SimpleMessage message = null;
	
	@Before
	public void beforeTest() {
		message = new SimpleMessage();
	}
	
	@Test
	public void getDateTest() {
		Date date = new Date(System.currentTimeMillis());
		message.setDate(date);
		Assert.assertEquals(date, message.getDate());
	}
}
