package org.dms;

import org.junit.Assert;
import org.junit.Test;

public class ManagingGetEverythingTest {
	@Test
	public void getEverythingTest() {
		Assert.assertNull(Managing.getEverything("Joe@mail.js"));
	}
}
