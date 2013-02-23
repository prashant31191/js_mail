package org.dms;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageGetMessToFoldsTest {
	private static Message message = null;
	
	@Before
	public void beforeTest() {
		message = new Message();
	}
	
	@Test
	public void getMessToFoldsTest() {
		MessToFold mtf = new MessToFold();
		mtf.setId(345);
		List<MessToFold> mtfs = new ArrayList<MessToFold>();
		mtfs.add(mtf);
		message.setMessToFolds(mtfs);
		Assert.assertTrue(345 == message.getMessToFolds().get(0).getId());
	}
}
