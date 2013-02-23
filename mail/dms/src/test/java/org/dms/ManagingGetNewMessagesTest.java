package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingGetNewMessagesTest {
	@Test
	public void getNewMessagesTest() {
		Assert.assertNull(Managing.getNewMessages(5, "Joe@mail.js"));
	}
}
