package org.dms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity for 'mess_fold' table in DB
 * 
 * @author Fomin
 * @version 1.0
 */
@Entity
@Table(name = "mess_fold")
public class MessToFold {
	@Id
	@Column(name = "mf_id")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "m_id")
	private Message message;
	
	@ManyToOne
    @JoinColumn(name = "f_id")
	private Folder folder;
	
	public MessToFold() {}	
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public Folder getFolder() {
		return folder;
	}
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	
}
