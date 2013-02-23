package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserGetFirstNameTest {
	private static User user = null;
	
	@Before
	public void beforeTest() {
		user = new User();
	}
	
	@Test
	public void getFirstNameTest() {
		user.setFirstName("Joe");
		Assert.assertEquals("Joe", user.getFirstName());
	}
}
