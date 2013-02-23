package org.dms;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetMessToFoldsTest {
	private static Folder folder = null;
	
	@Before
	public void beforeTest() {
		folder = new Folder();
	}
	
	@Test
	public void getMessToFoldsTest() {
		MessToFold mtf = new MessToFold();
		mtf.setId(345);
		List<MessToFold> mtfs = new ArrayList<MessToFold>();
		mtfs.add(mtf);
		folder.setMessToFolds(mtfs);
		Assert.assertTrue(345 == folder.getMessToFolds().get(0).getId());
	}
}
