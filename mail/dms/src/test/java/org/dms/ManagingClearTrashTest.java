package org.dms;

import java.util.List;

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingClearTrashTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = { "clearTrashTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(data);
	}

	@Test
	public void clearTrashIncorrectTest() {
		Assert.assertFalse(Managing.clearTrash("Joe@mail.js"));
	}

	@Test
	public void clearTrashCorrectTest() {
		List<SimpleFolder> folders = Managing
				.getEverything("clearTrashTest@mail.js");
		SimpleFolder inputFolder = null;
		for (SimpleFolder folder : folders) {
			if (folder.getName().equals("Входящие")) {
				inputFolder = folder;
				break;
			}
		}
		SimpleMessage message = inputFolder.getMessages().get(0);
		Managing.moveMessage(message, "Входящие", "Корзина",
				"setReadTest@mail.js");
		Assert.assertTrue(Managing.clearTrash("clearTrashTest@mail.js"));
	}
}
