package org.cc;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleFolderGetNameTest {
	private static SimpleFolder folder = null; 
	
	@Before
	public void beforeTest() {
		folder = new SimpleFolder();
	}
	
	@Test
	public void getNameTest() {
		folder.setName("Folder");
		Assert.assertEquals("Folder", folder.getName());
	}
}
