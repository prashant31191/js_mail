package org.cc;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageGetAboutTest {
	private static SimpleMessage message = null;
	
	@Before
	public void beforeTest() {
		message = new SimpleMessage();
	}
	
	@Test
	public void getAboutCorrectTest() {
		message.setAbout("Important");
		Assert.assertEquals("Important", message.getAbout());
	}
	
	@Test
	public void getAboutInorrectTest() {
		message.setAbout("Important");
		Assert.assertFalse("".equals(message.getAbout()));
	}
}
