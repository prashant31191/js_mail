package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class MessToFoldCreationTest {
	@Test
	public void creationTest() {
		MessToFold mtf = new MessToFold();
		Assert.assertNotNull(mtf);
	}
}
