package org.dms;

import org.cc.SimpleFolder;
import org.junit.Assert;
import org.junit.Test;

public class ManagingDeleteFolderTest {
	@Test
	public void deleteFolderTest() {
		Assert.assertTrue(2 == Managing.deleteFolder(new SimpleFolder(), "Joe@mail.js"));
	}
}
