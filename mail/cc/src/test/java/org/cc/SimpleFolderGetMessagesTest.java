package org.cc;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleFolderGetMessagesTest {
private static SimpleFolder folder = null; 
	
	@Before
	public void beforeTest() {
		folder = new SimpleFolder();
	}
	
	@Test
	public void getMessagesTest() {
		List<SimpleMessage> messages = new ArrayList<SimpleMessage>();
		SimpleMessage message = new SimpleMessage();
		messages.add(message);
		folder.setMessages(messages);
		System.out.println(folder.getMessages());
		System.out.println(messages);
		Assert.assertEquals(messages, folder.getMessages());
	}
}
