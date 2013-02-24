package org.cc;

import org.junit.Assert;
import org.junit.Test;

public class SerializerCreationTest {
	@Test
	public void creationTest() {
		Serializer serializer = new Serializer();
		Assert.assertNotNull(serializer);
	}
}
