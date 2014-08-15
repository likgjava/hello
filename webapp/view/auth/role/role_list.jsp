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
var RoleList = {};
RoleList.dataTable;

RoleList.selectResource = function(objId){
	new $.msgbox({
		title: '选择关联资源',
		width: 300,
		height: 400,
		type: 'ajax',
		content: $('#initPath').val()+'/view/auth/role/allot_resource.jsp?roleId='+objId,
		onAjaxed: function(){
			
		}
	}).show();
}

//修改
RoleList.updateRole = function(objId){
	$('#bodyBox').load($('#initPath').val()+'/RoleController.do?method=toRoleFormView', {objId: objId});
}

//删除
RoleList.deleteRole = function(objId){
	if(confirm('确认删除所选角色吗？')){
		$.getJSON($('#initPath').val()+'/RoleController.do?method=remove', {objId: objId}, function(json){
			if(json.success){
				RoleList.dataTable.reloadData();
			}
		});
	}
}

$(document).ready(function() {
	RoleList.dataTable = $('#roleList').myDataTable({
		singleSelect: false,
		queryColumns: 'objId,roleName,roleChName,createUser.userName,createTime',
		ajaxUrl: $('#initPath').val()+'/RoleController.do?method=list',
		searchZone: 'roleSearchForm',
		fnRowCallback: function(nRow, aData){
			var operStr = '<td><a href="javascript:" onclick="RoleList.updateRole(\''+aData.objId+'\')">修改</a>';
			operStr += ' <a href="javascript:" onclick="RoleList.deleteRole(\''+aData.objId+'\')">删除</a>';
			operStr += ' <a href="javascript:" onclick="RoleList.selectResource(\''+aData.objId+'\')">分配资源</a></td>';
			$(nRow).append(operStr);
			return nRow;
		}
	});


	$('#queryRoleBut').click(function(){
		//alert(myTable.dtSelects());
		RoleList.dataTable.reloadData();
	});


	
	//新增
	$('#addRoleBut').click(function(){
		$('#bodyBox').load($('#initPath').val()+'/RoleController.do?method=toRoleFormView');
	});

});
</script>
</head>

<body style="min-height:500px">
<input type="hidden" id="initPath" value="${initPath}" />
<div class="box-positon">
	<div class="rpos">当前位置: 角色管理 - 列表</div>
	<div class="ropt">
		<input type="button" id="addRoleBut" class="submit" value="新增" /> &nbsp; 
		<input type="button" id="deleteRoleBut" class="del-button" value="删除" /> &nbsp; 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="roleSearchForm" style="padding-top:5px;">
<div>
	角色名称: <input type="text" name="roleName" /><input type="hidden" name="roleName_op" value="like" />&nbsp;&nbsp;
	中文名称: <input type="text" name="roleChName" /><input type="hidden" name="roleChName_op" value="like" />
	<input type="button" id="queryRoleBut" value="查询" class="query" />
</div>
</form>

<table id="roleList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>ID</th>
	<th>角色名称</th>
	<th>角色中文名称</th>
	<th>创建人</th>
	<th>创建时间</th>
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