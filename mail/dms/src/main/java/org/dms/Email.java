package org.dms;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity for 'emails' table in DB
 * 
 * @author Fomin
 * @version 1.0
 */
@Entity
@Table(name = "emails")
public class Email {
	@Id
	@Column(name = "e_address", length = 30)
	private String eaddress;
	
	@Column(name = "e_cr_date")
	@Temporal(value=TemporalType.DATE)
	private Date creationDate;
	
	@ManyToOne
    @JoinColumn(name = "u_id")
	private User user;
	
	@Column(name = "e_password")
	private String password;
	
	@OneToMany(mappedBy="from")
    private List<Message> messages;
	
	@OneToMany(mappedBy="whose")
    private List<Folder> folders;
	
	@OneToMany(mappedBy="to")
    private List<MessToMail> MessToMails;
	
	public String getEaddress() {
		return eaddress;
	}
	public void setEaddress(String eaddress) {
		this.eaddress = eaddress;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Folder> getFolders() {
		return folders;
	}
	public void setFolders(List<Folder> folders) {
		this.folders = folders;
	}
	
}
