<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<%@ include file="/view/common/init.jsp" %>
<link href="${initPath}/view/resource/skin/jeecms/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="${initPath}/view/resource/skin/common/css/theme.css" rel="stylesheet" type="text/css"/>

<script src="${initPath}/view/resource/plug_in/jquery/myTableList/myTableList.js"></script>

<link rel="STYLESHEET" type="text/css" href="${initPath}/view/resource/plug_in/dhtmlxTree/dhtmlxtree.css">
<script src="${initPath}/view/resource/plug_in/dhtmlxTree/dhtmlxcommon.js"></script>
<script src="${initPath}/view/resource/plug_in/dhtmlxTree/dhtmlxtree.js"></script>

<script>
var ResourceTree = {};
$(document).ready(function() {

	ResourceTree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
	ResourceTree.setSkin('dhx_skyblue');
	ResourceTree.setImagePath($('#initPath').val()+"/view/resource/plug_in/dhtmlxTree/imgs/");
	ResourceTree.enableDragAndDrop(0);
	ResourceTree.setOnClickHandler(function(id){
		$('#resourceInfo').load($('#initPath').val()+'/ResourceController.do?method=toResourceDetailView', {objId:id});
	});
	ResourceTree.setXMLAutoLoading($('#initPath').val()+"/ResourceController.do?method=getChildrenXmlById");
	ResourceTree.loadXML($('#initPath').val()+"/ResourceController.do?method=getChildrenXmlById");


	//新增子节点
	$('#addResourceBut').click(function(){
		var id = ResourceTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要修改的节点！'); return ;
		}
		var data = {};
		if(id != '-1'){
			data.parentId = id;
		}

		//获取节点的层级数
		data.resourceLevel = ResourceTree.getLevel(id);
		
		$('#resourceInfo').load($('#initPath').val()+'/ResourceController.do?method=toResourceFormView', data);
	});

	//修改
	$('#updateResourceBut').click(function(){
		var id = ResourceTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要修改的节点！'); return ;
		}else if(id == '-1'){
			alert('该节点不能修改！'); return ;
		}
		$('#resourceInfo').load($('#initPath').val()+'/ResourceController.do?method=toResourceFormView', {objId:id});
	});
	
	//删除
	$('#deleteResourceBut').click(function(){
		var id = ResourceTree.getSelectedItemId();
		var msg = '确认删除该节点及其子节点吗？';
		if(id == '-1'){
			msg = '确认删除所有资源节点吗？';
		}
		if(confirm(msg)){
			$.getJSON($('#initPath').val()+'/ResourceController.do?method=removeAll', {objId: (id=='-1'?'':id)}, function(json){
				if(json.success){
					if(id != '-1'){
						var parentId = ResourceTree.getParentId(id);
						//选中父节点
						ResourceTree.selectItem(parentId);
						//刷新树节点
						ResourceTree.refreshItem(parentId);
						//刷新表单域
						$('#resourceInfo').load($('#initPath').val()+'/ResourceController.do?method=toResourceDetailView', {objId: parentId});
					}else{
						//刷新树节点
						ResourceTree.refreshItem('-1');
					}
				}
			});
		}
	});
	
} );
</script>
</head>

<body>
<input type="hidden" id="initPath" value="${initPath}" />
<div class="box-positon">
	<div class="rpos">当前位置: 栏目管理 - 列表</div>
	<form class="ropt">
		<input type="button" id="addResourceBut" class="submit" value="新增" /> &nbsp; 
		<input type="button" id="updateResourceBut" class="reset" value="修改" /> &nbsp; 
		<input type="button" id="deleteResourceBut" class="del-button" value="删除" /> &nbsp; 
	</form>
	<div class="clear"></div>
</div>


<div class="body-box">

<div style="width:20%; border: 1px solid #C8DCF0; float: left; margin-top:5px; margin-right:5px;">

<div id="treeboxbox_tree"></div>
</div>


<div id="resourceInfo" style="float: left; width:79%;">
</div>
</div>
<%@ include file="/view/auth/desktop/foot.jsp" %>
</body>
</html>