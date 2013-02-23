package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class MessToMailCreationTest {
	@Test
	public void creationTest() {
		MessToMail mtm = new MessToMail();
		Assert.assertNotNull(mtm);
	}
}
