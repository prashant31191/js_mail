package org.dms;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "messages")
public class Message {
	@Id
	@Column(name = "m_id")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "e_from")
	private Email from;
	
	@Column(name = "m_date")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date sentDate;
	
	@Column(name = "m_about")
	private String about;
	
	@Column(name = "m_text")
	private String text;
	
	@OneToMany(mappedBy="message")
    private Set<MessToFold> messToFolds;
	
	@OneToMany(mappedBy="tMessage")
    private Set<MessToMail> messToMails;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Email getEmail() {
		return from;
	}
	public void setEmail(Email from) {
		this.from = from;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
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
	
}