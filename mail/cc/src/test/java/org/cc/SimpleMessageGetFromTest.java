package org.cc;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageGetFromTest {
	private static SimpleMessage message = null;
	
	@Before
	public void beforeTest() {
		message = new SimpleMessage();
	}
	
	@Test
	public void getFromCorrectTest() {
		message.setFrom("Average Joe");
		Assert.assertEquals("Average Joe", message.getFrom());
	}
	
	@Test
	public void getFromInorrectTest() {
		message.setFrom("Average Joe");
		Assert.assertFalse("".equals(message.getFrom()));
	}
}
