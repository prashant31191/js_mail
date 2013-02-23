package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessToMailGetReadTest {
	private static MessToMail mtm = null;
	
	@Before
	public void beforeTest() {
		mtm = new MessToMail();
	}
	
	@Test
	public void getMessageTest() {
		mtm.setRead(true);
		Assert.assertTrue(mtm.getRead());
	}
}
