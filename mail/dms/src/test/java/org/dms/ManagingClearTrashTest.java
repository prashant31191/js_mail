package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingClearTrashTest {
	@Test
	public void clearTrashTest() {
		Assert.assertFalse(Managing.clearTrash("Joe@mail.js"));
	}
}
