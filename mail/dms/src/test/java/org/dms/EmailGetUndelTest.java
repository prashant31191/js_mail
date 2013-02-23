package org.dms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetUndelTest {
	private static Folder folder = null;
	
	@Before
	public void beforeTest() {
		folder = new Folder();
	}
	
	@Test
	public void getUndelTest() {
		folder.setUndel(true);
		Assert.assertTrue(folder.getUndel());
	}
}
