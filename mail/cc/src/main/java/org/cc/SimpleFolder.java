package org.cc;

import java.io.Serializable;
import java.util.List;

public class SimpleFolder implements Serializable{
	private String name;
	private List<SimpleMessage> messages;
	
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
		messages.add(0, message);
	}
}
