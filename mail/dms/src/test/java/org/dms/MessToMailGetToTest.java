package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessToMailGetToTest {
	private static MessToMail mtm = null;
	
	@Before
	public void beforeTest() {
		mtm = new MessToMail();
	}
	
	@Test
	public void getToTest() {
		Email email = new Email();
		email.setEaddress("Joe@mail.js");
		mtm.setTo(email);
		Assert.assertEquals("Joe@mail.js", mtm.getTo().getEaddress());
	}
}
