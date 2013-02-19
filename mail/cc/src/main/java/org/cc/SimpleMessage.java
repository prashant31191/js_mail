package org.cc;

import java.io.Serializable;
import java.util.Date;

/**
 * Common class with information about some user's message for communication
 * between server and client
 * 
 * @author Fomin
 * @version 1.0
 */
public class SimpleMessage implements Serializable, Comparable<SimpleMessage> {
	private String from;
	private String to;
	private Date date;
	private String about;
	private String text;
	private boolean read;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public int compareTo(SimpleMessage o) {
		if (this.getDate().compareTo(o.getDate()) < 0) {
			return 1;
		}
		if (this.getDate().compareTo(o.getDate()) > 0) {
			return -1;
		}
		return 0;
	}

}
