package com.likg.auth.controller;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.likg.auth.domain.Attachment;
import com.likg.core.controller.BaseGenericController;
import com.likg.core.util.AttachmentUtil;

@Controller
@RequestMapping("/AttachmentController.do")
public class AttachmentController extends BaseGenericController<Attachment>  {
	
	private static Logger log = Logger.getLogger(AttachmentController.class);
	
	/**
	 * 扩展CKEditor的图片上传功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=uploadCKEditorImg")
	public void uploadCKEditorImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//返回的信息
		String ckeditorFuncNum = request.getParameter("CKEditorFuncNum");
		String alertMsg = "";
		String imgPath = "";
		
		try {
			Attachment attachment = AttachmentUtil.uploadImage(request, "upload");
			imgPath = request.getContextPath() + "/" + attachment.getPath();
		} catch (Exception e) {
			alertMsg = e.getMessage();
			log.error("在CKEditor编辑器中上传图片时出现异常！", e);
		}
		
		String result = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction("+ckeditorFuncNum+", '"+imgPath+"', '"+alertMsg+"');</script>";
		Writer out = response.getWriter();
		out.write(result);
		out.flush();
		out.close();
	}

}
