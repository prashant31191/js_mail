package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingHasNewMessagesTest {
	@Test
	public void hasNewMessagesIncorrectUserTest() {
		Assert.assertTrue(-1 == Managing.hasNewMessages(5, "AverageJoe"));
	}
}
