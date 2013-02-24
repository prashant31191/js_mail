package org.dms;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingHasNewMessagesTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = {"hasNewTest", "12341234", "12341234", "Joe", "Johns", "11.12.1989", "463453"};
		Managing.createUser(data);
	}
	
	@Test
	public void hasNewMessagesIncorrectUserTest() {
		Assert.assertTrue(-1 == Managing.hasNewMessages(5, "AverageJoe"));
	}
	
	@Test
	public void hasNewMessagesCorrectUserTest() {
		Assert.assertTrue(1 == Managing.hasNewMessages(0, "hasNewTest@mail.js"));
	}
}
