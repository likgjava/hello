<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>top</title>
<%@ include file="/view/common/init.jsp" %>
<link href="${initPath}/view/resource/skin/jeecms/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="${initPath}/view/resource/skin/common/css/theme.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="${initPath}/view/resource/skin/jeecms/css/systop.css" />

<script>
function selectedCurrentMenu(obj){
	$(obj).siblings('li:not(.sep)').removeClass('current').addClass('normal');
	$(obj).removeClass('normal').addClass('current');
}

$(document).ready(function(){
	//获取菜单
	$.getJSON($('#initPath').val()+'/MenuController.do?method=getMenuListByUser',function(json){
		//拼装一级菜单
		var menuHtml = '';
		$(json.menuList).each(function(i,menu){
			//加装第一个菜单下的二级菜单
			if(i==0){
				window.open($('#initPath').val()+'/MenuController.do?method=toSecondLevelMenuListView&objId='+menu.objId, 'leftFrame');
			}
			//分割线
			else {
				menuHtml += '<li class="sep"></li>';
			}
			menuHtml += '<li onclick="selectedCurrentMenu(this);" id="'+menu.objId+'" class="normal"><a target="leftFrame" href="'+$('#initPath').val()+'/MenuController.do?method=toSecondLevelMenuListView&objId='+menu.objId+'">'+menu.menuName+'</a></li>';
		});
		$('#nav_menu_list').append(menuHtml);

		//默认选中第一个菜单
		$('#nav_menu_list li:eq(0)').click();
	});
});
</script> 
</head>

<body>
<input type="hidden" id="initPath" value="${initPath}" />
<div id="top">
<div class="top">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
	<tbody>
	<tr>
		<td width="215"><div class="logo"><img width="215" height="69" src="${initPath}/view/resource/skin/jeecms/img/admin/logo.png" /></div></td>
		<td valign="top">
			<div class="topbg">
				<div class="login-welcome">
					<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
					<tr>
						<td width="320" height="38">
							<img src="${initPath}/view/resource/skin/jeecms/img/admin/welconlogin-icon.png" /><span id="welcome">${user.userName }</span>
							<a href="${initPath}/LoginController.do?method=logout" target="_top"><img src="${initPath}/view/resource/skin/jeecms/img/admin/loginout-icon.png" />退出</a>&#12288;&#12288;
							<!-- 
							<img src="${initPath}/view/resource/skin/jeecms/img/admin/message-unread.png" />&nbsp;<a target="rightFrame" href="message/v_list.do">您有<span id="countDiv">0</span>条信息未读</a>
							-->
						</td>
						<!-- 
						<td align="right">
							<form method="get" target="_top" action="index.do">
								<select onchange="this.form.submit();" name="_site_id_param">
									<option selected="selected" value="1">JEECMS开发站</option>
									<option value="2">JEECMS子站点</option>
								</select>
							</form>
						</td>
						<td width="100">
							&nbsp;<a target="_blank" href="/" id="view_index">【查看首页】</a>
						</td>
						-->
					</tr>
					</tbody>
					</table>
				</div>  
				<div class="nav">
					<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
					<tr>
						<td width="14" height="31" style="background-image:url('${initPath}/view/resource/skin/jeecms/img/admin/nav-left.png')"></td>
						<td>
							<ul id="nav_menu_list" class="nav-menu"></ul>
						</td>
					</tr>
					</tbody>
					</table>
				</div>  
			</div>
		</td>
	</tr>
	</tbody>
</table>
</div>
</div>
<div class="top-bottom"></div>
</body>
</html>