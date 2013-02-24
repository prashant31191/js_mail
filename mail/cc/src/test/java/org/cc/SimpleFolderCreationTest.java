package org.cc;

import org.junit.Assert;
import org.junit.Test;

public class SimpleFolderCreationTest {
	@Test
	public void creationTest() {
		SimpleFolder folder = new SimpleFolder();
		Assert.assertNotNull(folder);
	}
}
