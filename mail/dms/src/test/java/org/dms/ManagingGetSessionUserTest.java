package org.dms;

import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ManagingGetSessionUserTest {
	private static int key = 0;
	
	@Before
	public void beforeTest() {
		Random rand = new Random();
		key = rand.nextInt(100000);
		Managing.createSession(key, "AverageJoe");
	}
	
	@Test
	public void getSessionUser() {
		Assert.assertEquals("AverageJoe", Managing.getSessionUser(key));
	}
	
	@After
	public void afterTest() {
		Managing.deleteSession(key);
	}
}
