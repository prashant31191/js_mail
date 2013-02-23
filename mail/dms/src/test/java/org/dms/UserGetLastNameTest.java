package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserGetLastNameTest {
	private static User user = null;
	
	@Before
	public void beforeTest() {
		user = new User();
	}
	
	@Test
	public void getLastNameTest() {
		user.setLastName("Joe");
		Assert.assertEquals("Joe", user.getLastName());
	}
}
