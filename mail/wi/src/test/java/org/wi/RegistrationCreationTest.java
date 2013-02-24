package org.wi;

import org.junit.Assert;
import org.junit.Test;

public class RegistrationCreationTest {
	@Test
	public void creationTest() {
		Registration registration = new Registration();
		Assert.assertNotNull(registration);
	}
}
