package org.business;

import junit.framework.Assert;
import org.junit.Test;

public class ServerCreationTest {
	@Test 
	public void creationTest() {
		Server server = new Server();
		Assert.assertNotNull(server);
	}
}
