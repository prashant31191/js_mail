package org.dms;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class ManagingCreateSessionTest {
	@Test
	public void createSession() {
		Random rand = new Random();
		Assert.assertTrue(Managing.createSession(rand.nextInt(10000), "AverageJoe"));
	}
}
