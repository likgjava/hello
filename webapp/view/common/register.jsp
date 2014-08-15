<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>会员注册</title>
<meta charset="utf-8" />
<%@ include file="/view/common/init.jsp" %>
<link rel="stylesheet" href="${initPath}/view/resource/skin/css/front.css" />
<link rel="stylesheet" href="${initPath}/view/resource/skin/css/layout.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/view/resource/skin/css/regist.css" />

<script type="text/javascript">
$(function() {
	//验证表单
	$("#registerForm").validate();

	//提交表单
	$('#registerForm').submit(function(){
		if(!$('#registerForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'json',
			success: function(json){
				if(json.result){
					$('#jcaptchaImg').click(); //刷新验证码
					alert(json.result);
				}else{
					alert('注册成功！');
					window.location.href = '/'; //跳转到首页
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
<body>
<div class="container">
<div class="regist-header box">
       <div class="rgheader box">
       <div class="brand fl">
       <h1><a href="${base}/">${site.name}11111</a></h1>
       </div>
       </div>
</div>
<div class="main">
	<table class="rg-tbg" cellspacing="0" cellpadding="0" width="971" align="center" border="0">
	<tbody>
		<tr>
			<td width="231" height="66"><h2>注册新会员</h2></td>
			<td align="right" width="740" style="font-size: 12px">您的位置：JEECMS官网&nbsp;&gt;&nbsp;<span style="color: #016dd0">注册会员</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2">
			<form id="registerForm" action="/RegisterController.do?method=saveRegisterUser" method="post" style="padding-top: 20px">
				<table cellspacing="5" cellpadding="0" width="850" align="center" border="0">
				<tbody>
					<tr>
						<td align="right" width="187" height="30">用户名：</td>
						<td width="648"><input type="text" id="userName" name="userName" class="input2 {required:true, userName:true, rangelength:[3,20], remote:'/RegisterController.do?method=userNameUnique',messages:{remote:'用户名已被占用！'}}" /></td>
					</tr>
					<tr>
						<td align="right" width="187" height="30">&nbsp;</td>
						<td width="648">用户名由3到20位的中文字符、英文字母、数字组成。</td>
					</tr>
					<tr>
						<td align="right" height="30">电子邮箱：</td>
						<td><input class="input2 {required:true, email:true, remote:'/RegisterController.do?method=emailUnique',messages:{remote:'email已经被使用！'}}" id="email" maxlength="30" name="email" type="text" /></td>
					</tr>
					<tr>
						<td align="right" height="30">&nbsp;</td>
						<td width="648">请准确填入您的邮箱，在忘记密码，或者您使用邮件通知功能时，会发送邮件到该邮箱。</td>
					</tr>
					<tr>
						<td align="right" height="30">密 码：</td>
						<td><input class="input2 required" id="password" type="password" name="password" /></td>
					</tr>
					<tr>
						<td align="right" height="30">确认密码：</td>
						<td><input class="input2 {equalTo:'#password'}" type="password" /></td>
					</tr>
					<tr>
						<td align="right" bgcolor="#ffffff" height="30">验证码：</td>
						<td><input class="input required" maxlength="20" name="captcha" type="text" /></td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td><img id="jcaptchaImg" src="/jcaptcha" onclick="this.src='/jcaptcha?d='+new Date()" /></td>
					</tr>
					<tr>
						<td align="center" colspan="2" height="40"><input class="regist-submit" type="submit" name="register" value=" " /></td>
					</tr>
				</tbody>
				</table>
			</form>
			</td>
		</tr>
	</tbody>
	</table>
</div>
</div>
</body>
</html>