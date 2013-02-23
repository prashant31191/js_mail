package org.dms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class FolderGetIdTest {
	private static Folder folder = null;
	
	@Before
	public void beforeTest() {
		folder = new Folder();
	}
	
	@Test
	public void getIdTest() {
		folder.setId(1234);
		Assert.assertTrue(1234 == folder.getId());
	}
}
