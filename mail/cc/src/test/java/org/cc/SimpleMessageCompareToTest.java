package org.cc;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageCompareToTest {
	private static SimpleMessage mess1 = null;
	private static SimpleMessage mess2 = null;
	
	@Before
	public void beforeTest() {
		mess1 = new SimpleMessage();
		mess1.setDate(new Date(System.currentTimeMillis()));
		mess2 = new SimpleMessage();
		mess2.setDate(new Date(System.currentTimeMillis() + 1000));
	}
	
	@Test
	public void compareToMoreTest() {
		Assert.assertTrue(1 == mess1.compareTo(mess2));
	}
	
	@Test
	public void compareToLessTest() {
		Assert.assertTrue(-1 == mess2.compareTo(mess1));
	}
	
	@Test
	public void compareToEqualsTest() {
		Assert.assertTrue(0 == mess1.compareTo(mess1));
	}
}
