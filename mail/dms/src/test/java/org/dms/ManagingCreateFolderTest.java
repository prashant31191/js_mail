package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingCreateFolderTest {
	@Test
	public void createFolderTest() {
		Assert.assertNull(Managing.createFolder("Folder", "Joe@mail.js"));
	}
}
