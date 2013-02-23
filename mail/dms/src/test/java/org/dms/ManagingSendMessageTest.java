package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingSendMessageTest {
	@Test
	public void sendMessage() {
		Assert.assertNull(Managing.sendMessage(new String[1], "Joe@mail.js"));
	}
}
