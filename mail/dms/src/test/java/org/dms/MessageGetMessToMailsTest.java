package org.dms;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageGetMessToMailsTest {
	private static Message message = null;
	
	@Before
	public void beforeTest() {
		message = new Message();
	}
	
	@Test
	public void getEmailTest() {
		MessToMail mtm = new MessToMail();
		mtm.setId(123);
		List<MessToMail> mtms = new ArrayList<MessToMail>();
		mtms.add(mtm);
		message.setMessToMails(mtms);
		Assert.assertTrue(123 == message.getMessToMails().get(0).getId());
	}
}
