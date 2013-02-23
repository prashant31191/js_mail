package org.dms;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionGetTimeTest {
	private static Session session = null;
	
	@Before
	public void beforeTest() {
		session = new Session();
	}
	
	@Test
	public void getTimeTest() {
		Date date = new Date(System.currentTimeMillis());
		session.setTime(date);
		Assert.assertEquals(date, session.getTime());
	}
}
