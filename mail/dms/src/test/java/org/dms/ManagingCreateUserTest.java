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
}
