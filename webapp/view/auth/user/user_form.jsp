<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/view/common/taglibs.jsp" %>

<form id="userForm" action="${initPath}/UserController.do?method=save" method="post">
	<input type="hidden" id="userObjId" name="objId" value="${user.objId }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tbody>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>用户名:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" name="userName" maxlength="100" class="{required:true, userName:true, rangelength:[3,20], remote:'/RegisterController.do?method=userNameUnique&ignoreObjId=${user.objId}',messages:{remote:'用户名已被占用！'}}" value="${user.userName }"></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>邮箱:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" name="email" class="{required:true, email:true, remote:'/RegisterController.do?method=emailUnique&ignoreObjId=${user.objId}',messages:{remote:'email已经被使用！'}}" value="${user.email }"></td>
		</tr>
		<tr>
			<td class="pn-fbutton" colspan="4"><input type="submit" class="submit" value="提交" /> &nbsp; <input type="reset" class="reset" value="重置" /></td>
		</tr>
	</tbody>
	</table>
</form>

<script type="text/javascript">
$(document).ready(function() {
	//验证表单
	$("#userForm").validate();

	//提交表单
	$('#userForm').submit(function(){
		if(!$('#userForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'json',
			success: function(json){
				if(json.success){
					window.open($('#initPath').val()+'/view/auth/user/user_list.jsp', 'rightFrame');
				}else{
					alert(json.result);
				}
			},
			error: function(msg){
				alert(msg);
			}
		});

		//(重要)always return false to prevent standard browser submit and page navigation 
		return false;
	});
} );
</script>
