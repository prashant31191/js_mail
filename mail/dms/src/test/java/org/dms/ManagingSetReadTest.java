package org.dms;

import java.util.List;

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingSetReadTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = { "setReadTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(data);
	}

	@Test
	public void setReadIncorrectTest() {
		Assert.assertFalse(Managing.setRead(new SimpleMessage(), "Joe@mail.js"));
	}

	@Test
	public void setReadCorrectTest() {
		List<SimpleFolder> folders = Managing
				.getEverything("setReadTest@mail.js");
		SimpleFolder inputFolder = null;
		for (SimpleFolder folder : folders) {
			if (folder.getName().equals("Входящие")) {
				inputFolder = folder;
				break;
			}
		}
		SimpleMessage message = inputFolder.getMessages().get(0);
		Assert.assertTrue(Managing.setRead(message, "setReadTest@mail.js"));
	}
}
