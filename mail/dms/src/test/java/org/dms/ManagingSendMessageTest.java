package org.dms;

import java.util.ArrayList;
import java.util.List;

import org.cc.SimpleMessage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagingSendMessageTest {
	@BeforeClass
	public static void beforeTest() {
		String[] dataSender = {"sendMessageGetterTest", "12341234", "12341234", "Joe", "Johns", "11.12.1989", "463453"};
		String[] dataGetter = {"sendMessageSenderTest", "12341234", "12341234", "Joe", "Johns", "11.12.1989", "463453"};
		Managing.createUser(dataGetter);
		Managing.createUser(dataSender);
	}
	
	
	@Test
	public void sendMessageIncorrectTest() {
		Assert.assertNull(Managing.sendMessage(new String[1], "Joe@mail.js"));
	}
	
	@Test
	public void sendMessageCorrectTest() {
		String[] data = {"sendMessageGetterTest@mail.js;noreply@mail.js", "Hi", "Hi there"};
		Object[] gotData = Managing.sendMessage(data, "sendMessageSenderTest@mail.js");
		SimpleMessage outMess = (SimpleMessage)gotData[0];
		List<SimpleMessage> inMesses = (ArrayList<SimpleMessage>)gotData[1];
		Assert.assertTrue((outMess != null) && (inMesses.size() == 1));
	}
}
