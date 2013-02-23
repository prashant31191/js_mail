package org.dms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class FolderGetNameTest {
	private static Folder folder = null;
	
	@Before
	public void beforeTest() {
		folder = new Folder();
	}
	
	@Test
	public void getNameTest() {
		folder.setName("Входящие");
		Assert.assertEquals("Входящие", folder.getName());
	}
}
