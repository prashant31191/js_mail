package org.dms;

import java.util.List;

import org.cc.SimpleFolder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingGetEverythingTest {
	@BeforeClass
	public static void beforeTest() {
		String[] data = { "getEverythingTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(data);
		String[] getterData = { "geTTerTest", "12341234", "12341234", "Joe",
				"Johns", "11.12.1989", "463453" };
		Managing.createUser(getterData);
	}

	@Test
	public void getEverythingIncorrectTest() {
		Assert.assertNull(Managing.getEverything("Joe@mail.js"));
	}

	@Test
	public void getEverythingCorrectTest() {
		Managing.sendMessage(new String[] { "geTTerTest@mail.js", "Hi",
				"Hi there" }, "getEverythingTest@mail.js");
		List<SimpleFolder> folders = Managing
				.getEverything("getEverythingTest@mail.js");
		Assert.assertTrue(3 == folders.size());
	}
}
