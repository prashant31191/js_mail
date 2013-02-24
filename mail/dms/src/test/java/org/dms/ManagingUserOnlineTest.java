package org.dms;

import java.util.Random;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingUserOnlineTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = {"userOnlineTest", "12341234", "12341234", "Joe", "Johns", "11.12.1989", "463453"};
		Managing.createUser(data);
	}
	
	@Test
	public void userOnlineIncorrectTest() {
		Assert.assertFalse(Managing.userOnline("userOnlineTest@mail.js"));
	}
	
	@Test
	public void userOnlineCorrectTest() {
		Random rand = new Random();
		Managing.createSession(rand.nextInt(100000), "userOnlineTest@mail.js");
		Assert.assertTrue(Managing.userOnline("userOnlineTest@mail.js"));
	}
}
