package com.likg.auth.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.likg.core.domain.BaseObject;

@Entity
@Table(name="AUTH_USER")
public class User implements BaseObject, UserDetails {

	private static final long serialVersionUID = -1529875094776536954L;

	/**记录号*/
	@Id
	@Column(name="USER_ID", length=50)
	@GeneratedValue(generator="generatorUUID")
	@GenericGenerator(name="generatorUUID", strategy="uuid")
	private String objId;
	
	/** 用户名 */
	@Column(name="USER_NAME", length=20)
	private String userName;
	
	/*密码*/
	@Column(name="USER_PASSWORD", length=50)
	private String password;
	
	/*真实姓名*/
	@Column(name="USER_REAL_NAME", length=20)
	private String realName;
	
	/**邮箱*/
	@Column(name="USER_EMAIL", length=20)
	private String email;
	
	/*性别*/
	@Column(name="USER_SEX", length=1)
	private Boolean sex;
	
	/*年龄*/
	@Column(name="USER_AGE")
	private BigDecimal age;
	
	/*照片*/
	@Column(name="USER_PHOTO", length=50)
	private String photo;
	
	/**是否为管理员(0:不是;1:是)*/
	@Column(name="USER_IS_ADMIN", length=20)
	private String isAdmin;
	
	/*使用状态*/
	@Column(name="USE_STATUS", length=2)
	private String useStatus;
	
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	/*用户角色中间表*/
	@JsonIgnore //当生成JSON的时候忽略该字段
	@ManyToMany//(fetch=FetchType.EAGER)//(cascade={javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.REFRESH}, fetch=FetchType.EAGER)
	//@LazyCollection(LazyCollectionOption.FALSE)
	@Cache(region="com.likg.auth.domain.Role", usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinTable(name="AUTH_USER_ROLE", joinColumns=@JoinColumn(name="USER_ID"), inverseJoinColumns=@JoinColumn(name="ROLE_ID"))
	private Set<Role> roles = new HashSet<Role>();
	
	/**重写Spring security中UserDetails的方法*/
	@Transient
	private Set<GrantedAuthority> authorities;
	@Transient
	private boolean accountNonExpired;
	@Transient
	private boolean accountNonLocked;
	@Transient
	private boolean credentialsNonExpired;
	@Transient
	private boolean enabled;
	
	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public BigDecimal getAge() {
		return age;
	}

	public void setAge(BigDecimal age) {
		this.age = age;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
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

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getUsername() {
		return this.userName;
	}

	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}
	
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}
	
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}
	
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
