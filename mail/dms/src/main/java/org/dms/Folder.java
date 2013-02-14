package org.dms;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity for 'folders' table in DB
 * 
 * @author Fomin
 * @version 1.0
 */
@Entity
@Table(name = "folders")
public class Folder {
	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "f_name", length = 30)
	private String name;
	
	@ManyToOne
    @JoinColumn(name = "e_address")
	private Email whose;
	
	@Column(name = "f_undel")
	private Boolean undel;
	
	@OneToMany(mappedBy="folder")
    private List<MessToFold> messToFolds;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Email getWhose() {
		return whose;
	}
	public void setWhose(Email whose) {
		this.whose = whose;
	}
	public Boolean getUndel() {
		return undel;
	}
	public void setUndel(Boolean undel) {
		this.undel = undel;
	}

	public List<MessToFold> getMessToFolds() {
		return messToFolds;
	}
	public void setMessToFolds(List<MessToFold> messToFolds) {
		this.messToFolds = messToFolds;
	}
	
}
