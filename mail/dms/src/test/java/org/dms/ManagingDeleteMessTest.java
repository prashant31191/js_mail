package org.dms;

import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.Test;

public class ManagingDeleteMessTest {
	@Test
	public void deleteMessTest() {
		Assert.assertFalse(Managing.deleteMess(new SimpleMessage(), "Корзина",
				"Joe@mail.js"));
	}
}
