package org.jboss.examples.conferences.domain;

// Generated Oct 31, 2014 3:44:38 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Conference generated by hbm2java
 */
@Entity
@Table(name = "CONFERENCE", schema = "PUBLIC", catalog = "CONFERENCES")
public class Conference implements java.io.Serializable {

	private long id;
	private Integer version;
	private Date endDate;
	private String location;
	private String name;
	private Date startDate;
	private Set<Session> sessions = new HashSet<Session>(0);

	public Conference() {
	}

	public Conference(long id) {
		this.id = id;
	}

	public Conference(long id, Date endDate, String location, String name,
			Date startDate, Set<Session> sessions) {
		this.id = id;
		this.endDate = endDate;
		this.location = location;
		this.name = name;
		this.startDate = startDate;
		this.sessions = sessions;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 8)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "LOCATION")
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 8)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conference")
	public Set<Session> getSessions() {
		return this.sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}

}
