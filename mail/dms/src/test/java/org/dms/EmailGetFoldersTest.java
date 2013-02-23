package org.dms;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetFoldersTest {
	private static Email email = null;

	@Before
	public void beforeTest() {
		email = new Email();
	}

	@Test
	public void getFoldersTest() {
		Folder folder = new Folder();
		folder.setName("Входящие");
		List<Folder> folders = new ArrayList<Folder>();
		folders.add(folder);

		email.setFolders(folders);
		Assert.assertEquals("Входящие", email.getFolders().get(0).getName());
	}
}
