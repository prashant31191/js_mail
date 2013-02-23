package org.dms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessToFoldGetFolderTest {
	private static MessToFold mtf = null;
	
	@Before
	public void beforeTest() {
		mtf = new MessToFold();
	}
	
	@Test
	public void getFolderTest() {
		Folder folder = new Folder();
		folder.setName("Папка");
		mtf.setFolder(folder);
		Assert.assertEquals("Папка", mtf.getFolder().getName());
	}
}
