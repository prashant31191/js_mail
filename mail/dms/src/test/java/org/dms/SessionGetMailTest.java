package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionGetMailTest {
	private static Session session = null;
	
	@Before
	public void beforeTest() {
		session = new Session();
	}
	
	@Test
	public void getMailTest() {
		session.setMail("Joe@mail.js");
		Assert.assertEquals("Joe@mail.js", session.getMail());
	}
}
