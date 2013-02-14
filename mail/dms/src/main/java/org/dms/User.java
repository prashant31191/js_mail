package org.dms;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity for 'users' table in DB
 * 
 * @author Fomin
 * @version 1.0
 */
@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(name = "u_id")
	@GeneratedValue
	private Integer id;
	
	@Column(name = "u_fname", length = 30)
	private String firstName;
	
	@Column(name = "u_sname", length = 30)
	private String lastName;
	
	@Column(name = "u_bdate")
	@Temporal(value=TemporalType.DATE)
	private Date birthDate;
	
	@Column(name = "u_phone", length = 20)
	private String phone;
	
	@OneToMany(mappedBy="user")
    private List<Email> emails;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
