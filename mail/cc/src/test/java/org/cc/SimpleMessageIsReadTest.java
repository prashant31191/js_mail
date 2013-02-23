package org.cc;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageIsReadTest {
	private static SimpleMessage message = null;
	
	@Before
	public void beforeTest() {
		message = new SimpleMessage();
	}
	
	@Test
	public void isReadTrueTest() {
		message.setRead(true);
		Assert.assertTrue(message.isRead());
	}
	
	@Test
	public void isReadFalseTest() {
		message.setRead(false);
		Assert.assertFalse(message.isRead());
	}
}
