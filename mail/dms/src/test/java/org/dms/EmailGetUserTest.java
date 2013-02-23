package org.dms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetUserTest {
	private static Email email = null;
	@Before
	public void beforeTest() {
		email = new Email();
	}
	
	@Test
	public void getUserTest() {
		User user = new User();
		user.setFirstName("Average Joe");
		email.setUser(user);
		Assert.assertEquals("Average Joe", email.getUser().getFirstName());
	}
}
