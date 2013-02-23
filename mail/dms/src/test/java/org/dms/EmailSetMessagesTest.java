package org.dms;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EmailSetMessagesTest {
	private static Email email = null;
	@Before
	public void beforeTest() {
		email = new Email();
	}
	
	@Test
	public void getMessagesTest() {
		Message message = new Message();
		message.setText("Hi");
		List<Message> messages = new ArrayList<Message>();
		messages.add(message);
		email.setMessages(messages);
		Assert.assertEquals("Hi", email.getMessages().get(0).getText());
	}
}
