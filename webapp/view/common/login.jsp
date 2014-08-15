<%@ page contentType="text/html; charset=UTF-8" %>
<%
session.setAttribute("initPath", request.getContextPath());
%>
<html>
<head>
<meta charset="utf-8" />
<title>会员登录</title>
<%@ include file="/view/common/init.jsp" %>
<link rel="stylesheet" href="${initPath}/view/resource/skin/css/front.css" />
<link rel="stylesheet" href="${initPath}/view/resource/skin/css/layout.css" />
<link rel="stylesheet" href="${initPath}/view/resource/skin/css/regist.css" />
<script>
//如果是在Frame中打开此页面，则重新在浏览器中打开登录页面（避免在后台操作，当session失效时，在Frame中打开登录页面）
function escapeFrame(){
	if(window.top.location.href != window.location.href){
		window.top.location.href = $('#initPath').val()+'/view/auth/login/login.jsp';
	}
}

$(function() {
	$("#loginForm").validate();

	//提交表单
	$('#loginForm').submit(function(){
		if(!$('#loginForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'json',
			success: function(json){
				if(json.success){
					window.location.href = $('#initPath').val()+'/MemberController.do?method=toManageCenterView'; //跳转到管理员首页
				}else{
					$('#jcaptchaImg').click(); //刷新验证码
					$('#captcha').val('');
					$('#message').html(json.result);
				}
			},
			error: function(msg){
				alert(msg);
			}
		});

		//(重要)always return false to prevent standard browser submit and page navigation 
		return false;
	});
});
</script>
</head>
<body onload="escapeFrame()">
<input type="hidden" id="initPath" value="${initPath}" />
<div class="container">
<div class="main">  
	<div class="header box">
        <div class="brand">
            <h1><a href="${initPath}/"></a></h1>
        </div>
	</div>
	<form id="loginForm" action="${initPath}/j_spring_security_check" method="post">
	<table width="800" border="0" align="center" cellpadding="0" cellspacing="5">		  
		<tr>
			<td height="30" align="left">
				<div style="color:red; margin-left:450px; font-weight:bold;" id="message"></div>
			</td>
		</tr>
	</table>
	<table width="900" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td width="500" height="300" align="left"><img src="${initPath}/view/resource/skin/img/member/llogo.gif" /></td>
            <td>
            	<div class="login-bg">
				<table width="96%" border="0" align="center" cellpadding="0" cellspacing="5">
					<tr>
						<td height="40" colspan="3"><strong style="font-size:14px;">会员登录</strong></td>
					</tr>
					<tr>
						<td width="67" height="30" align="right">用户名：</td>
						<td colspan="2"><input type="text" id="userName" name="userName" required="true" class="input" value="admin"/></td>
					</tr>
					<tr>
						<td height="30" align="right">密　码：</td>
						<td colspan="2"><input type="password" id="password" name="password" required="true" class="input" value="1"/></td>
					</tr>
					<tr>
						<td height="30" align="right">验证码：</td>
						<td ><input type="text" id="captcha" name="captcha" required="true" class="input"/></td>
					</tr>
					<tr>
						<td height="30" align="right">&nbsp;</td>
						<td colspan="2"><img id="jcaptchaImg" src="${initPath}/jcaptcha" onclick="this.src='${initPath}/jcaptcha?d='+new Date()*1" width="100" height="35"/></td>
					</tr>
					<tr>
						<td height="40" colspan="3" align="center"><input type="submit" value=" 登 录 " class="login-button"/>&nbsp;&nbsp;&nbsp;<a href="" target="_blank" class="forgot-password">忘记密码？</a></td>
					</tr>
					<tr>
						<td height="50" colspan="3" align="center" style="font-size:12px; color:#404040;">我还没有帐号？<a href="${initPath}/RegisterController.do?method=toRegisterView" style="color:#1647a6;">马上注册</a></td>
					</tr>
				</table>
				</div>
			</td>
		</tr>
	</table>
	</form>	
</div>
</div>
</body>
</html>