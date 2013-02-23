package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessToMailGetIdTest {
	private static MessToMail mtm = null;
	
	@Before
	public void beforeTest() {
		mtm = new MessToMail();
	}
	
	@Test
	public void getIdTest() {
		mtm.setId(564);
		Assert.assertTrue(564 == mtm.getId());
	}
}
