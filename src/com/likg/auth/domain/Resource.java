package com.likg.auth.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.likg.core.domain.BaseTree;

@Entity
@Table(name="AUTH_RESOURCE")
@Cache(region="com.likg.auth.domain.Resource", usage=CacheConcurrencyStrategy.READ_WRITE)
public class Resource implements BaseTree<Resource> {

	private static final long serialVersionUID = -1047723815794609153L;

	/**记录号*/
	@Id
	@Column(name="RES_ID", length=50)
	private String objId;
	
	/**父资源*/
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="RES_PARENT_ID")
	private Resource parent;
	
	/**资源名称*/
	@Column(name="RES_NAME", length=20)
	private String resName;
	
	/**资源路径*/
	@Column(name="RES_URL", length=100)
	private String resUrl;
	
	/**资源描述*/
	@Column(name="RES_DESC", length=100)
	private String resDesc;
	
	/**资源级别*/
	@Column(name="TREE_LEVEL")
	private Short treeLevel;
	
	/**是否叶子节点*/
	@Column(name="IS_LEAF")
	private Boolean isLeaf;
	
	/**创建人*/
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="CREATE_USER_ID")
	private User createUser;
	
	/**创建时间*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	/**角色资源中间表*/
	@ManyToMany
	@JoinTable(name="AUTH_ROLE_RESOURCE", joinColumns=@JoinColumn(name="RES_ID"), inverseJoinColumns=@JoinColumn(name="ROLE_ID"))
	private Set<Role> roles = new HashSet<Role>();

	@Transient
	private Set<Resource> children = new HashSet<Resource>();

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public Short getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(Short treeLevel) {
		this.treeLevel = treeLevel;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Resource> getChildren() {
		return children;
	}

	public void setChildren(Set<Resource> children) {
		this.children = children;
	}

}
