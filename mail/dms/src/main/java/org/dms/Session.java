package org.dms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity for 'sessions' table in DB
 * 
 * @author Fomin
 * @version 1.0
 */
@Entity
@Table(name = "sessions")
public class Session {
	@Id
	@Column(name = "s_id")
	private Integer id;

	@Column(name = "s_mail", length = 30)
	private String mail;

	@Column(name = "s_date")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	
}