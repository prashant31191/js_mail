package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingCheckUserTest {
	@Test
	public void checkUser() {
		Assert.assertFalse(Managing.checkUser("Joe@mail.js", "23542342"));
	}
}
