<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
<title></title>
<%@ include file="/view/common/init.jsp" %>
<%@ include file="/view/auth/desktop/head.jsp" %>
<script src="${initPath}/view/resource/plug_in/jquery/myTableList/myTableList.js" type="text/javascript"></script>

<link rel="stylesheet" href="${initPath}/view/resource/plug_in/dhtmlxTree/dhtmlxtree.css" />
<script src="${initPath}/view/resource/plug_in/dhtmlxTree/dhtmlxcommon.js"></script>
<script src="${initPath}/view/resource/plug_in/dhtmlxTree/dhtmlxtree.js"></script>

<script type="text/javascript" charset="utf-8">
var MenuTree = {};
$(document).ready(function() {

	MenuTree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
	MenuTree.setSkin('dhx_skyblue');
	MenuTree.setImagePath($('#initPath').val()+"/view/resource/plug_in/dhtmlxTree/imgs/");
	MenuTree.enableDragAndDrop(0);
	MenuTree.setOnClickHandler(function(id){
		$('#menuInfo').load($('#initPath').val()+'/MenuController.do?method=toMenuDetailView', {objId:id});
	});
	//tree.enableTreeLines(false);
	//MenuTree.insertNewItem(null,'-1','aa');
	//MenuTree.setImageArrays("plus", "", "", "", "plus.gif");
	//MenuTree.setImageArrays("minus", "", "", "", "minus.gif");
	//MenuTree.setStdImages("book.gif", "books_open.gif", "books_close.gif");
	MenuTree.setXMLAutoLoading($('#initPath').val()+"/MenuController.do?method=getChildrenXmlById");
	//MenuTree.loadXML("/view/auth/sys/desktop/tree_b.xml");
	MenuTree.loadXML($('#initPath').val()+"/MenuController.do?method=getChildrenXmlById");


	//新增子节点
	$('#addMenuBut').click(function(){
		var id = MenuTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要修改的节点！'); return ;
		}
		var data = {};
		if(id != '-1'){
			data.parentId = id;
		}

		//获取节点的层级数
		data.menuLevel = MenuTree.getLevel(id);
		
		$('#menuInfo').load($('#initPath').val()+'/MenuController.do?method=toMenuFormView', data);
	});

	//修改
	$('#updateMenuBut').click(function(){
		var id = MenuTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要修改的节点！'); return ;
		}else if(id == '-1'){
			alert('该节点不能修改！'); return ;
		}
		$('#menuInfo').load($('#initPath').val()+'/MenuController.do?method=toMenuFormView', {objId:id});
	});
	
	//删除
	$('#deleteMenuBut').click(function(){
		var id = MenuTree.getSelectedItemId();
		var msg = '确认删除该节点及其子节点吗？';
		if(id == '-1'){
			msg = '确认删除所有菜单节点吗？';
		}
		if(confirm(msg)){
			$.getJSON($('#initPath').val()+'/MenuController.do?method=removeAll', {objId: (id=='-1'?'':id)}, function(json){
				if(json.success){
					if(id != '-1'){
						var parentId = MenuTree.getParentId(id);
						//选中父节点
						MenuTree.selectItem(parentId);
						//刷新树节点
						MenuTree.refreshItem(parentId);
						//刷新表单域
						$('#menuInfo').load($('#initPath').val()+'/MenuController.do?method=toMenuDetailView', {objId: parentId});
					}else{
						//刷新树节点
						MenuTree.refreshItem('-1');
					}
				}
			});
		}
	});
	
} );
</script>
</head>

<body>
<div class="box-positon">
	<div class="rpos">当前位置: 栏目管理 - 列表</div>
	<form class="ropt">
		<input type="button" id="addMenuBut" class="submit" value="新增" /> &nbsp; 
		<input type="button" id="updateMenuBut" class="reset" value="修改" /> &nbsp; 
		<input type="button" id="deleteMenuBut" class="del-button" value="删除" /> &nbsp; 
	</form>
	<div class="clear"></div>
</div>

<div class="body-box" style="overflow:hidden;">
	<div style="width:20%; border: 1px solid #C8DCF0; float: left; margin-top:5px; margin-right:5px;">
		<div id="treeboxbox_tree"></div>
	</div>
	
	<div id="menuInfo" style="float: left; width:79%;"></div>
</div>
<%@ include file="/view/auth/desktop/foot.jsp" %>
</body>
</html>