package org.dms;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailGetCreationDateTest {
	private static Email email = null;
	@Before
	public void beforeTest() {
		email = new Email();
	}
	
	@Test
	public void getCreationDateTest() {
		Date date = new Date(System.currentTimeMillis());
		email.setCreationDate(date);
		Assert.assertEquals(date, email.getCreationDate());
	}
}
