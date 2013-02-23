package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageGetTextTest {
	private static Message message = null;
	
	@Before
	public void beforeTest() {
		message = new Message();
	}
	
	@Test
	public void getTextTest() {
		message.setText("Hi there");
		Assert.assertEquals("Hi there", message.getText());
	}
}
