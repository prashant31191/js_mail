package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageGetAboutTest {
	private static Message message = null;
	
	@Before
	public void beforeTest() {
		message = new Message();
	}
	
	@Test
	public void getIdTest() {
		message.setAbout("Important");
		Assert.assertEquals("Important", message.getAbout());
	}
}
