package com.likg.auth.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.likg.core.domain.BaseObject;

@Entity
@Table(name="BASE_ATTACHMENT")
public class Attachment implements BaseObject {

	private static final long serialVersionUID = 1L;
	
	public static final String PICTURE = "picture";
	public static final String WORD = "word";

	/**记录号*/
	@Id
	@Column(name="ATT_ID", length=50)
	@GeneratedValue(generator="generatorUUID")
	@GenericGenerator(name="generatorUUID", strategy="uuid")
	private String objId;
	
	/**文件名*/
	@Column(name="FILE_NAME", length=20)
	private String fileName;
	
	/**文件大小*/
	@Column(name="FILE_SIZE", length=50)
	private Long fileSize;
	
	/**文件类型*/
	@Column(name="FILE_TYPE", length=20)
	private String fileType;
	
	/**文件描述*/
	@Column(name="FILE_DESC", length=20)
	private String fileDesc;
	
	/**路径*/
	@Column(name="PATH", length=50)
	private String path;
	
	/**上传IP*/
	@Column(name="UPLOAD_IP", length=20)
	private String uploadIp;
	
	/**创建人*/
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="CREATE_USER_ID")
	private User createUser;
	
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUploadIp() {
		return uploadIp;
	}

	public void setUploadIp(String uploadIp) {
		this.uploadIp = uploadIp;
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

}
