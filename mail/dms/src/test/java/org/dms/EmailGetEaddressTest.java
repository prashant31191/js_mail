package org.dms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetEaddressTest {
	private static Email email = null;
	@Before
	public void beforeTest() {
		email = new Email();
	}
	
	@Test
	public void getEaddressTest() {
		email.setEaddress("test@mail.js");
		Assert.assertEquals("test@mail.js", email.getEaddress());
	}
}
