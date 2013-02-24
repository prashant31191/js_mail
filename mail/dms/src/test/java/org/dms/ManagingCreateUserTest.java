package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingCreateUserTest {
	@Test
	public void createUserExceptionTest() {
		try {
			Managing.createUser(new String[5]);
		} catch (ArrayIndexOutOfBoundsException e) {
			Assert.assertTrue(e instanceof ArrayIndexOutOfBoundsException);
		}
	}
	
	@Test
	public void createUserIncorrectUserTest() {
		String[] data = {"noreply", "", "", "", "", "", ""};
		Assert.assertEquals("", Managing.createUser(data));
	}
	
	@Test
	public void createUserWrongDataTest() {
		String[] data = {"test", "", "", "", "", "sfsfe", ""};
		try {
			Managing.createUser(data);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof Exception);
		}
	}
	
	@Test
	public void createUserTest() {
		String[] data = {"test", "", "", "Joe", "Johns", "11.12.1989", "463453"};
		Assert.assertEquals("test@mail.js", Managing.createUser(data));
	}
}
