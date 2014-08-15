<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
<title></title>
<%@ include file="/view/common/init.jsp" %>
<link href="${initPath}/view/resource/skin/jeecms/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="${initPath}/view/resource/skin/common/css/theme.css" rel="stylesheet" type="text/css"/>

<script src="${initPath}/view/resource/scripts/json_util.js" type="text/javascript"></script>
<script src="${initPath}/view/resource/plug_in/jquery/myTableList/myTableList.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
var UserList = {};
UserList.dataTable;

//修改
UserList.updateUser = function(objId){
	$('#bodyBox').load($('#initPath').val()+'/UserController.do?method=toUserFormView&objId='+objId);
}

//删除
UserList.deleteUser = function(objId){
	if(!confirm('确认删除该用户吗？')){return;}
	$.getJSON($('#initPath').val()+'/UserController.do?method=remove&objId='+objId, function(json){
		if(json.success){
			UserList.dataTable.reloadData();
		}
	});
}

//分配角色
UserList.allotRole = function(objId){
	new $.msgbox({
		title: '分配角色',
		width: 500,
		height: 400,
		type: 'ajax',
		content: $('#initPath').val()+'/RoleController.do?method=toAllotRoleView&userId='+objId,
		onAjaxed: function(){
			
		}
	}).show();
}

$(document).ready(function() {
	UserList.dataTable = $('#userList').myDataTable({
		singleSelect: false,
		queryColumns: 'objId,userName,email,createTime',
		ajaxUrl: $('#initPath').val()+'/UserController.do?method=list',
		searchZone: 'userSearchForm',
		fnRowCallback: function(nRow, aData){
			var operStr = '<td><a href="javascript:" onclick="UserList.allotRole(\''+aData.objId+'\');">分配角色</a>';
			operStr += '&nbsp;<a href="javascript:" onclick="UserList.updateUser(\''+aData.objId+'\');">修改</a>';
			operStr += '&nbsp;<a href="javascript:" onclick="UserList.deleteUser(\''+aData.objId+'\');">删除</a></td>';
			$(nRow).append(operStr);
			return nRow;
		}
	});


	$('#queryUserBut').click(function(){
		//alert(myTable.dtSelects());
		UserList.dataTable.reloadData();
	});


	
	/**
	$('#expertInfoManageList').dataTable({
		'singleSelect' : true,
		'checkbox' : false,
		'queryColumns' : 'name,belongIndustry.name,createUser.usName,createTime,auditStatus,isOff',
		'alias':'name,belongIndustry.name,createUser.usName,createTime,auditStatusCN,isOffCN',
		'hiddenColumns':'useStatus',
		'fnInitComplete' : function(oSettings) {
		},
		'fnDrawCallback' : function(oSettings) {
			ExpertInfoManageList.oTable.oSettings = oSettings;
		},
		'fnRowCallback' : function(nRow, aData, iDisplayIndex) {
			var str = ExpertInfoManageList.getOperationStr(aData.objId, aData.useStatus, aData.isOff);
			$(nRow).append(str);
			
			return nRow;
		},
		"params":{"useStatus":"00"},
		"sAjaxSource" : $('#initPath').val()+ "/ExpertInfoController.do?method=list&type=manage",
		'searchZone':'expertInfoManageListForm'
	});
	*/

	//新增
	$('#addUserBut').click(function(){
		$('#bodyBox').load($('#initPath').val()+'/UserController.do?method=toUserFormView');
	});

});
</script>
</head>

<body>
<input type="hidden" id="initPath" value="${initPath}" />
<div class="box-positon">
	<div class="rpos">当前位置: 用户管理 - 列表</div>
	<div class="ropt">
		<input type="button" id="addUserBut" class="submit" value="新增" /> &nbsp; 
		<input type="button" id="deleteMenuBut" class="del-button" value="删除" /> &nbsp; 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="userSearchForm" style="padding-top:5px;">
<div>
	用户名: <input type="text" name="userName" /><input type="hidden" name="userName_op" value="like" />
	<input type="button" id="queryUserBut" value="查询" class="query" />
</div>
</form>

<table id="userList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>ID</th>
	<th>用户名</th>
	<th>邮箱</th>
	<th>注册时间</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
</tbody>
</table>
</div>
<%@ include file="/view/auth/desktop/foot.jsp" %>
</body>
</html>