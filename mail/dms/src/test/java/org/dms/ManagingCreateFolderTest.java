package org.dms;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingCreateFolderTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = { "createFolderTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(data);
	}

	@Test
	public void createFolderIncorrectTest() {
		Assert.assertNull(Managing.createFolder("Folder", "Joe@mail.js"));
	}

	@Test
	public void createFolderCorrectTest() {
		Assert.assertNotNull(Managing.createFolder("Folder",
				"createFolderTest@mail.js"));
	}
}
