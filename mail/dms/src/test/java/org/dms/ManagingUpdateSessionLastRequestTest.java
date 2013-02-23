package org.dms;

import java.util.Random;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingUpdateSessionLastRequestTest {
	private static int key = 0;
	
	@BeforeClass
	public static void beforeTest() {
		Random rand = new Random();
		key = rand.nextInt(100000);
		Managing.createSession(key, "AverageJoe");
	}
	
	@Test
	public void getSessionExistsCorrectTest() {
		Assert.assertTrue(Managing.undateSessionLastRequest(key));
	}
	
	@Test
	public void sessionExistsIncorrectTest() {
		Assert.assertFalse(Managing.undateSessionLastRequest(-1));
	}
	
	@AfterClass
	public static void afterTest() {
		Managing.deleteSession(key);
	}
}
