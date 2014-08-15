package com.likg.auth.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.likg.core.domain.BaseObject;

@Entity
@Table(name="AUTH_ROLE")
@Cache(region="com.likg.auth.domain.Role", usage=CacheConcurrencyStrategy.READ_WRITE)
public class Role implements BaseObject {

	private static final long serialVersionUID = -8261510674390717923L;

	/**记录号*/
	@Id
	@Column(name="ROLE_ID", length=50)
	@GeneratedValue(generator="generatorUUID")
	@GenericGenerator(name="generatorUUID", strategy="uuid")
	private String objId;
	
	/**角色名称*/
	@Column(name="ROLE_NAME", length=20)
	private String roleName;
	
	/**角色中文名称*/
	@Column(name="ROLE_CH_NAME", length=40)
	private String roleChName;
	
	/*角色描述*/
	@Column(name="ROLE_DESC", length=100)
	private String roleDesc;
	
	/*创建人*/
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="CREATE_USER_ID")
	private User createUser;
	
	/*创建时间*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	/*用户角色中间表*/
	@ManyToMany(cascade={CascadeType.REMOVE, CascadeType.PERSIST})
	@BatchSize(size=30)
	//@LazyCollection(LazyCollectionOption.EXTRA)
	//@Cascade(CascadeType.REMOVE)
	@JoinTable(name="AUTH_USER_ROLE", joinColumns=@JoinColumn(name="ROLE_ID"), inverseJoinColumns=@JoinColumn(name="USER_ID"))
	private Set<User> users = new HashSet<User>();
	
	/*角色资源中间表*/
	@ManyToMany
	@JoinTable(name="AUTH_ROLE_RESOURCE", joinColumns=@JoinColumn(name="ROLE_ID"), inverseJoinColumns=@JoinColumn(name="RES_ID"))
	private Set<Resource> resources = new HashSet<Resource>();

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleChName() {
		return roleChName;
	}

	public void setRoleChName(String roleChName) {
		this.roleChName = roleChName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	
}
