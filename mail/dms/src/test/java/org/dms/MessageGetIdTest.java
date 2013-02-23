package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageGetIdTest {
	private static Message message = null;
	
	@Before
	public void beforeTest() {
		message = new Message();
	}
	
	@Test
	public void getIdTest() {
		message.setId(543);
		Assert.assertTrue(543 == message.getId());
	}
}
