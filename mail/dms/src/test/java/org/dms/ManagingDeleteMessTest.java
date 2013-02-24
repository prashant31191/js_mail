package org.dms;

import java.util.List;

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingDeleteMessTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = {"delMessTest", "12341234", "12341234", "Joe", "Johns", "11.12.1989", "463453"};
		Managing.createUser(data);
	}
	
	@Test
	public void deleteMessIncorrectTest() {
		Assert.assertFalse(Managing.deleteMess(new SimpleMessage(), "Корзина",
				"Joe@mail.js"));
	}
	
	@Test
	public void deleteMessCorrectTest() {
		List<SimpleFolder> folders = Managing.getEverything("delMessTest@mail.js");
		SimpleFolder inputFolder = null;
		for (SimpleFolder folder : folders) {
			if (folder.getName().equals("Входящие")) {
				inputFolder = folder;
				break;
			}
		}
		SimpleMessage message = inputFolder.getMessages().get(0);
		Assert.assertTrue(Managing.deleteMess(message, "Входящие", "delMessTest@mail.js"));
	}
}
