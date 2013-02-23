package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessToFoldGetIdTest {
	private static MessToFold mtf = null;
	
	@Before
	public void beforeTest() {
		mtf = new MessToFold();
	}
	
	@Test
	public void getIdTest() {
		mtf.setId(1234);
		Assert.assertTrue(1234 == mtf.getId());
	}
}
