package org.dms;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserGetBirthDateTest {
	private static User user = null;
	
	@Before
	public void beforeTest() {
		user = new User();
	}
	
	@Test
	public void getBirthDateTest() {
		Date date = new Date(System.currentTimeMillis());
		user.setBirthDate(date);
		Assert.assertEquals(date, user.getBirthDate());
	}
}
