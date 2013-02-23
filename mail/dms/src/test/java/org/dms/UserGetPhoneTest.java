package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserGetPhoneTest {
	private static User user = null;
	
	@Before
	public void beforeTest() {
		user = new User();
	}
	
	@Test
	public void getPhoneTest() {
		user.setPhone("+654534");
		Assert.assertEquals("+654534", user.getPhone());
	}
}
