package org.cc;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageGetToTest {
	private static SimpleMessage message = null;
	
	@Before
	public void beforeTest() {
		message = new SimpleMessage();
	}
	
	@Test
	public void getToCorrectTest() {
		message.setTo("Average Joe");
		Assert.assertEquals("Average Joe", message.getTo());
	}
	
	@Test
	public void getToIncorrectTest() {
		message.setTo("Average Joe");
		Assert.assertFalse("".equals(message.getTo()));
	}
}
