package org.dms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Entity for 'mess_mail' table in DB
 * 
 * @author Fomin
 * @version 1.0
 */
@Entity
@Table(name = "mess_mail")
public class MessToMail {
	@Id
	@Column(name = "t_id")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "m_id")
	private Message tMessage;
	
	@ManyToOne
    @JoinColumn(name = "e_address")
	private Email to;
	
	@Column(name = "t_read")
	private Boolean read;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Message getMessage() {
		return tMessage;
	}
	public void setMessage(Message tMessage) {
		this.tMessage = tMessage;
	}
	public Email getTo() {
		return to;
	}
	public void setTo(Email to) {
		this.to = to;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	
	
}
