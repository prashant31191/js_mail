package org.dms;

import java.util.List;

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingMoveMessageTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = { "moveMessTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(data);
	}

	@Test
	public void moveMessageIncorrectTest() {
		Assert.assertFalse(Managing.moveMessage(new SimpleMessage(), "Folder1",
				"Folder2", "Joe@mail.js"));
	}

	@Test
	public void moveMessageCorrectTest() {
		List<SimpleFolder> folders = Managing
				.getEverything("moveMessTest@mail.js");
		SimpleFolder inputFolder = null;
		for (SimpleFolder folder : folders) {
			if (folder.getName().equals("Входящие")) {
				inputFolder = folder;
				break;
			}
		}
		SimpleMessage message = inputFolder.getMessages().get(0);

		Assert.assertTrue(Managing.moveMessage(message, "Входящие", "Корзина",
				"moveMessTest@mail.js"));
	}
}
