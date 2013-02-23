package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionGetIdTest {
	private static Session session = null;
	
	@Before
	public void beforeTest() {
		session = new Session();
	}
	
	@Test
	public void getIdTest() {
		session.setId(123);
		Assert.assertTrue(123 == session.getId());
	}
}
