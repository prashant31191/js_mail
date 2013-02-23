package org.dms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetPasswordTest {
	private static Email email = null;
	@Before
	public void beforeTest() {
		email = new Email();
	}
	
	@Test
	public void getPasswordTest() {
		email.setPassword("w4fgg%gf3");
		Assert.assertEquals("w4fgg%gf3", email.getPassword());
	}
}
