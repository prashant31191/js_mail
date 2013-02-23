package org.dms;

import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.Test;

public class ManagingSetReadTest {
	@Test
	public void setRead() {
		Assert.assertFalse(Managing.setRead(new SimpleMessage(), "Joe@mail.js"));
	}
}
