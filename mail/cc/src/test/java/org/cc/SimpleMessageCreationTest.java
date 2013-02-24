package org.cc;

import org.junit.Assert;
import org.junit.Test;

public class SimpleMessageCreationTest {
	@Test
	public void creationTest() {
		SimpleMessage message = new SimpleMessage();
		Assert.assertNotNull(message);
	}
}
