package org.dms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetWhoseTest {
	private static Folder folder = null;
	
	@Before
	public void beforeTest() {
		folder = new Folder();
	}
	
	@Test
	public void getWhoseTest() {
		Email email = new Email();
		email.setEaddress("Joe@mail.js");
		folder.setWhose(email);
		Assert.assertEquals("Joe@mail.js", folder.getWhose().getEaddress());
	}
}
