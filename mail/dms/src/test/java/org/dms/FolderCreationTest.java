package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class FolderCreationTest {
	@Test
	public void creationTest() {
		Folder folder = new Folder();
		Assert.assertNotNull(folder);
	}
}
