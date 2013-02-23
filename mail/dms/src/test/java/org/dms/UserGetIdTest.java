package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserGetIdTest {
	private static User user = null;
	
	@Before
	public void beforeTest() {
		user = new User();
	}
	
	@Test
	public void getIdTest() {
		user.setId(123);
		Assert.assertTrue(123 == user.getId());
	}
}
