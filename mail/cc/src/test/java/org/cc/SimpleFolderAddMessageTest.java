package org.cc;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleFolderAddMessageTest {
	private static SimpleFolder folder = null;
	private static SimpleMessage earlyMessage = null;
	private static SimpleMessage lateMessage = null;
	private static SimpleMessage middleMessage = null;
	
	@Before
	public void beforeTest() {
		folder = new SimpleFolder();
		earlyMessage = new SimpleMessage();
		earlyMessage.setText("Early");
		earlyMessage.setDate(new Date(System.currentTimeMillis()));
		lateMessage = new SimpleMessage();
		lateMessage.setText("Late");
		lateMessage.setDate(new Date(System.currentTimeMillis() + 100000));
		middleMessage = new SimpleMessage();
		middleMessage.setText("Middle");
		middleMessage.setDate(new Date(System.currentTimeMillis() + 10000));
	}
	
	@Test
	public void addLateMessageTest() {
		folder.addMessage(earlyMessage);
		folder.addMessage(lateMessage);
		Assert.assertEquals("Late", folder.getMessages().get(0).getText());
	}
	
	@Test
	public void addMiddleMessageTest() {
		folder.addMessage(earlyMessage);
		folder.addMessage(lateMessage);
		folder.addMessage(middleMessage);
		Assert.assertEquals("Middle", folder.getMessages().get(1).getText());
	}
}
