package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class SessionCreationTest {
	@Test
	public void creationTest() {
		Session session = new Session();
		Assert.assertNotNull(session);
	}
}
