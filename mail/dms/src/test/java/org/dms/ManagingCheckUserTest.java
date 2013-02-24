package org.dms;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingCheckUserTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = {"createUserTest", "12345678", "12345678", "Joe", "Johns", "11.12.1989", "463453"};
		Managing.createUser(data);
	}
	
	@Test
	public void checkIncorrectUser() {
		Assert.assertFalse(Managing.checkUser("Joe@mail.js", "23542342"));
	}
	
	@Test
	public void checkCorrectUser() {
		Assert.assertTrue(Managing.checkUser("createUserTest@mail.js", "12345678"));
	}
}
