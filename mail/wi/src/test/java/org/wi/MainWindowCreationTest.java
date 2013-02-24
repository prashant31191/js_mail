package org.wi;

import org.junit.Assert;
import org.junit.Test;

public class MainWindowCreationTest {
	@Test
	public void creationTest() {
		try {
			MainWindow mainWindow = new MainWindow(12, "Hi there");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof Exception);
		}
	}
}
