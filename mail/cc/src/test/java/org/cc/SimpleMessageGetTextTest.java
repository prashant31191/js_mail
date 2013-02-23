package org.cc;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageGetTextTest {
	private static SimpleMessage message = null;
	
	@Before
	public void beforeTest() {
		message = new SimpleMessage();
	}
	
	@Test
	public void getTextCorrectTest() {
		message.setText("SomeText");
		Assert.assertEquals("SomeText", message.getText());
	}
	
	@Test
	public void getTextIncorrectTest() {
		message.setText("SomeText");
		Assert.assertFalse("".equals(message.getText()));
	}
}
