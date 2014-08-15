<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/view/common/taglibs.jsp" %>

<input type="hidden" id="userId" value="${userId }" />
<table id="allotRoleList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>状态</th>
	<th>ID</th>
	<th>角色名称</th>
	<th>角色中文名称</th>
</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="role" items="${allRoleList}">
	<tr>
		<c:set var="allotted" value="false" />
		<c:forEach var="allottedRoleId" items="${allottedRoleIds}">
			<c:if test="${allottedRoleId == role.objId}">
				<c:set var="allotted" value="true" />
			</c:if>
		</c:forEach>
		<td align="center"><input type="checkbox" name="allotStatus" value="${role.objId}" <c:if test="${allotted}">checked="checked"</c:if> /></td>
		<td>${role.objId }</td>
		<td>${role.roleName }</td>
		<td>${role.roleChName }</td>
	</tr>
	</c:forEach>
</tbody>
</table>

<div style="margin-top:15px;">
	<input type="button" class="submit" id="allotRoleBut" value="保存" />
	<input type="button" class="reject" onclick="common.closeMsgbox();" value="取消" />
</div>

<script>
$(document).ready(function() {
	
	//保存
	$('#allotRoleBut').click(function(){
		var roleIds = [];
		$('#allotRoleList tbody').find('input[name=allotStatus]:checked').each(function(i,n){
			roleIds.push($(n).val());
		});
		$.getJSON('/RoleController.do?method=allotRole',{userId: $('#userId').val(), roleIds:roleIds},function(json){
			if(json.success){
				alert('保存成功！');
				common.closeMsgbox();
			}
		});

	});

});
</script>
