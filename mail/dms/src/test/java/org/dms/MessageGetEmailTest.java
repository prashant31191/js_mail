package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageGetEmailTest {
	private static Message message = null;
	
	@Before
	public void beforeTest() {
		message = new Message();
	}
	
	@Test
	public void getEmailTest() {
		Email email = new Email();
		email.setEaddress("Joe@mail.js");
		message.setEmail(email);
		Assert.assertEquals("Joe@mail.js", message.getEmail().getEaddress());
	}
}
