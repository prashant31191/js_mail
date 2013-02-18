package org.cc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SimpleFolder implements Serializable{
	private String name;
	private List<SimpleMessage> messages = new ArrayList<SimpleMessage>();
	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SimpleMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<SimpleMessage> messages) {
		this.messages = messages;
	}
	public void addMessage(SimpleMessage message) {
		int index = 0;
		for (SimpleMessage tmpMess : messages) {
			if (message.getDate().compareTo(tmpMess.getDate()) >= 0) {
				break;
			} else index++;
		}
		messages.add(index, message);
	}
}
