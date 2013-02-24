package org.dms;

import java.util.List;

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingDeleteFolderTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = { "delFoldTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(data);
	}

	@Test
	public void deleteFolderIncorrectDataTest() {
		Assert.assertTrue(2 == Managing.deleteFolder(new SimpleFolder(),
				"Joe@mail.js"));
	}

	@Test
	public void deleteFolderIncorrectFolderTest() {
		List<SimpleFolder> folders = Managing
				.getEverything("delFoldTest@mail.js");
		SimpleFolder inputFolder = null;
		for (SimpleFolder folder : folders) {
			if (folder.getName().equals("Входящие")) {
				inputFolder = folder;
				break;
			}
		}
		Assert.assertTrue(1 == Managing.deleteFolder(inputFolder,
				"delFoldTest@mail.js"));
	}

	@Test
	public void deleteFolderCorrectFolderTest() {
		SimpleFolder plainFolder = Managing.createFolder("PlainFolder",
				"delFoldTest@mail.js");

		List<SimpleFolder> folders = Managing
				.getEverything("delFoldTest@mail.js");
		SimpleFolder inputFolder = null;
		for (SimpleFolder folder : folders) {
			if (folder.getName().equals("Входящие")) {
				inputFolder = folder;
				break;
			}
		}
		SimpleMessage message = inputFolder.getMessages().get(0);
		Managing.moveMessage(message, "Входящие", "PlainFolder",
				"delFoldTest@mail.js");

		Assert.assertTrue(0 == Managing.deleteFolder(plainFolder,
				"delFoldTest@mail.js"));
	}
}
