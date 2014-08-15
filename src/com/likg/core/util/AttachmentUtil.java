package com.likg.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.likg.auth.domain.Attachment;
import com.likg.auth.service.AttachmentService;
import com.likg.common.Constants;
import com.likg.core.context.FrameBeanFactory;
import com.likg.core.context.FrameMessageSource;
import com.likg.security.AuthenticationHelper;

/**
 * 附件工具类
 * @author likg
 */
public class AttachmentUtil {
	
	
	/**
	 * 上传图片
	 * @param request
	 * @param fileInputName 表单中<input type='file'>的name名称
	 * @return
	 * @throws Exception
	 */
	public static Attachment uploadImage(HttpServletRequest request, String fileInputName) throws Exception {
		Attachment attachment = null;
		//获取附件对象
		MultipartFile file = AttachmentUtil.getMultipartFile(request, fileInputName);
		
		//获取附件信息
		String originalFilename = file.getOriginalFilename(); //原文件名
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1); //后缀名
		if(AttachmentUtil.getEnableImgSuffixList().contains(suffix.toUpperCase())) {
			attachment = AttachmentUtil.uploadFile(request, file, Attachment.PICTURE);
		} else {
			throw new IllegalArgumentException("请选择正确的图片格式(jpg,jpeg,png,gif)！");
		}
		
		return attachment;
	}
	
	/**
	 * 保存上传附件
	 * @param request MultipartHttpServletRequest类型的request
	 * @param fileInputName 表单中<input type='file'>的name名称
	 * @return
	 * @throws Exception
	 */
	public static Attachment uploadFile(HttpServletRequest request, String fileInputName) throws Exception {
		Attachment attachment = null;
		//获取附件对象
		MultipartFile file = AttachmentUtil.getMultipartFile(request, fileInputName);
		attachment = AttachmentUtil.uploadFile(request, file, "");
		
		return attachment;
	}
	
	/**
	 * 保存上传附件
	 * @param request
	 * @param file 附件对象
	 * @param fileType 文件类型
	 * @return
	 * @throws Exception
	 */
	private static Attachment uploadFile(HttpServletRequest request, MultipartFile file, String fileType) throws Exception {
		//获取附件信息
		String originalFilename = file.getOriginalFilename(); //原文件名
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1); //后缀名
		
		//把附件保存到硬盘
		FrameMessageSource messageSource = (FrameMessageSource) FrameBeanFactory.getBean(FrameMessageSource.BEAN_NAME);
		String uuidName = UUID.randomUUID().toString() + "." + suffix; //新文件名称
		String filePath = messageSource.getMessage("imgUploadPath") + "/" + uuidName;
		File localFile = new File(Constants.ROOTPATH + filePath);
		file.transferTo(localFile);
		
		//保存附件对象
		Attachment attachment = new Attachment();
		attachment.setFileName(originalFilename);
		attachment.setFileSize(file.getSize());
		attachment.setFileType(fileType);
		attachment.setUploadIp(request.getRemoteAddr());
		attachment.setPath(filePath);
		attachment.setCreateUser(AuthenticationHelper.getCurrentUser());
		AttachmentService attachmentService = (AttachmentService) FrameBeanFactory.getBean("attachmentServiceImpl");
		attachmentService.save(attachment);
		
		return attachment;
	}
	
	/**
	 * 获取附件对象
	 * @param request
	 * @param fileInputName 表单中<input type='file'>的name名称
	 * @return
	 */
	private static MultipartFile getMultipartFile(HttpServletRequest request, String fileInputName) {
		MultipartFile file = null;
		
		if(!(request instanceof MultipartHttpServletRequest)) {
			throw new IllegalArgumentException("该request不是MultipartHttpServletRequest类型！");
		}
		
		//判断是否存在上传附件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		file = multipartRequest.getFile(fileInputName);
		if(file==null || file.isEmpty()) {
			throw new IllegalArgumentException("上传附件为空！");
		}
		
		return file;
	}
	
	/**
	 * 获取允许上传的图片后缀名
	 * @return
	 */
	private static List<String> getEnableImgSuffixList() {
		List<String> enableSuffixList = new ArrayList<String>();
		enableSuffixList.add("JPG");
		enableSuffixList.add("JPEG");
		enableSuffixList.add("PNG");
		enableSuffixList.add("GIF");
		return enableSuffixList;
	}
}
