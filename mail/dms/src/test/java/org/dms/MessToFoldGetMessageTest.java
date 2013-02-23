package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessToFoldGetMessageTest {
	private static MessToFold mtf = null;
	
	@Before
	public void beforeTest() {
		mtf = new MessToFold();
	}
	
	@Test
	public void getMessageTest() {
		Message message = new Message();
		message.setAbout("Hi");
		mtf.setMessage(message);
		Assert.assertEquals("Hi", mtf.getMessage().getAbout());
	}
}
