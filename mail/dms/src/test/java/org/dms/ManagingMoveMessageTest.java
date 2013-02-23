package org.dms;

import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.Test;

public class ManagingMoveMessageTest {
	@Test
	public void moveMessageTest() {
		Assert.assertFalse(Managing.moveMessage(new SimpleMessage(), "Folder1",
				"Folder2", "Joe@mail.js"));
	}
}
