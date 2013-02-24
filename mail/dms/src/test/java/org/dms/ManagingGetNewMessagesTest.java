package org.dms;

import java.util.List;

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingGetNewMessagesTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = { "getNewTest", "12341234", "12341234", "Joe", "Johns",
				"11.12.1989", "463453" };
		Managing.createUser(data);
		String[] getterData = { "geTTerTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(getterData);
	}

	@Test
	public void getNewMessagesIncorrectTest() {
		Assert.assertNull(Managing.getNewMessages(5, "Joe@mail.js"));
	}

	@Test
	public void hetNewMessagesCorrectUserTest() {
		Managing.sendMessage(new String[] { "geTTerTest@mail.js", "Hi",
				"Hi there" }, "getNewTest@mail.js");
		List<SimpleFolder> folders = Managing
				.getEverything("getNewTest@mail.js");
		SimpleFolder inputFolder = null;
		for (SimpleFolder folder : folders) {
			if (folder.getName().equals("Исходящие")) {
				inputFolder = folder;
				break;
			}
		}
		SimpleMessage message = inputFolder.getMessages().get(0);
		Managing.moveMessage(message, "Исходящие", "Входящие",
				"getNewTest@mail.js");
		List<SimpleMessage> messages = Managing.getNewMessages(2,
				"getNewTest@mail.js");

		Assert.assertTrue(2 == messages.size());
	}
}
