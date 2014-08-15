<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/view/common/taglibs.jsp" %>

<form id="menuForm" action="${initPath}/MenuController.do?method=save" method="post">
	<input type="hidden" id="menuObjId" name="objId" value="${menu.objId }" />
	<input type="hidden" id="parentMenuObjId" name="parent.objId" value="${menu.parent.objId }" />
	<input type="hidden" name="treeLevel" value="${menu.treeLevel }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tbody>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">上级菜单:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><span>${menu.parent.menuName }</span></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>菜单名称:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" name="menuName" maxlength="100" class="required" value="${menu.menuName }"></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>关联资源:</td>
			<td width="88%" class="pn-fcontent" colspan="3">
				<input type="hidden" id="resourceId" name="resource.objId" value="${menu.resource.objId }" />
				<input type="text" id="resourceName" class="required" value="${menu.resource.resName }" />
			</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">菜单样式:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" name="menuCss" maxlength="100" value="${menu.menuCss }"></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">菜单描述:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><textarea maxlength="255" name="menuDesc" rows="4" cols="60">${menu.menuDesc }</textarea></td>
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
	$("#menuForm").validate();

	//选择关联资源
	$('#resourceName').click(function(){
		new $.msgbox({
			title: '选择关联资源11',
			width: 300,
			height: 400,
			type: 'ajax',
			content: $('#initPath').val()+'/view/auth/common/tree/DHtmlTree.jsp?className=Resource&IDS=resourceId&NAMES=resourceName',
			onAjaxed: function(){
				
			}
		}).show();
	});

	//提交表单
	$('#menuForm').submit(function(){
		if(!$('#menuForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'json',
			success: function(json){
				if(json.success){
					var menuObjId = $('#menuObjId').val();
					//修改
					if(menuObjId){
						//更新节点名称
						MenuTree.setItemText(menuObjId, $('input[name=menuName]', '#menuForm').val(), '');
						//刷新表单域
						$('#menuInfo').load($('#initPath').val()+'/MenuController.do?method=toMenuDetailView', {objId: menuObjId});
					}
					//新增
					else{
						//刷新树节点
						MenuTree.refreshItem(MenuTree.getSelectedItemId());
						//刷新表单域
						$('#menuInfo').load($('#initPath').val()+'/MenuController.do?method=toMenuDetailView', {objId: MenuTree.getSelectedItemId()});
					}
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
