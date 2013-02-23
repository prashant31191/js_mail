package org.dms;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmailGetMessToMailsTest {
	private static Email email = null;
	@Before
	public void beforeTest() {
		email = new Email();
	}
	
	@Test
	public void getMessToMailsTest() {
		MessToMail mtm = new MessToMail();
		mtm.setRead(true);
		List<MessToMail> mtms = new ArrayList<MessToMail>();
		mtms.add(mtm);
		email.setMessToMails(mtms);
		Assert.assertTrue(email.getMessToMails().get(0).getRead());
	}
}
