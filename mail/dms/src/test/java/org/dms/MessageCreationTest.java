package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class MessageCreationTest {
	@Test
	public void creationTest() {
		Message message = new Message();
		Assert.assertNotNull(message);
	}
}
