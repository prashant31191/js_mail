package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessToMailGetMessageTest {
	private static MessToMail mtm = null;
	
	@Before
	public void beforeTest() {
		mtm = new MessToMail();
	}
	
	@Test
	public void getMessageTest() {
		Message message = new Message();
		message.setText("Hi");
		mtm.setMessage(message);
		Assert.assertEquals("Hi", mtm.getMessage().getText());
	}
}
