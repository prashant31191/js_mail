package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class EmailCreationTest {
	@Test
	public void creationTest() {
		Email email = new Email();
		Assert.assertNotNull(email);
	}
}
