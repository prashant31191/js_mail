package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class UserCreationTest {
	@Test
	public void creationTest() {
		User user = new User();
		Assert.assertNotNull(user);
	}
}
