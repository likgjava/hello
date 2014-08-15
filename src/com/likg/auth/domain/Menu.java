package com.likg.auth.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.likg.core.domain.BaseTree;

@Entity
@Table(name="AUTH_MENU")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Menu implements BaseTree<Menu> {

	private static final long serialVersionUID = 1638300741655156017L;

	/**记录号*/
	@Id
	@Column(name="MENU_ID", length=50)
	private String objId;
	
	/**父菜单*/
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="MENU_PARENT_ID")
	private Menu parent;
	
	/**关联资源*/
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="RES_ID")
	private Resource resource;
	
	/**菜单名称*/
	@Column(name="MENU_NAME", length=20)
	private String menuName;
	
	/*菜单描述*/
	@Column(name="MENU_DESC", length=100)
	private String menuDesc;
	
	/**菜单级别*/
	@Column(name="TREE_LEVEL")
	private Short treeLevel;
	
	/**是否叶子节点*/
	@Column(name="IS_LEAF")
	private Boolean isLeaf;
	
	/**菜单样式*/
	@Column(name="MENU_CSS", length=20)
	private String menuCss;
	
	/**创建人*/
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="CREATE_USER_ID")
	private User createUser;
	
	/**创建时间*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Transient
	private Set<Menu> children = new HashSet<Menu>();

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
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

	public String getMenuCss() {
		return menuCss;
	}

	public void setMenuCss(String menuCss) {
		this.menuCss = menuCss;
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

	public Set<Menu> getChildren() {
		return children;
	}

	public void setChildren(Set<Menu> children) {
		this.children = children;
	}

}
