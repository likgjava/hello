

(function($){
	
	//选中多行的id字符串，多个以逗号分割
	$.fn.dtSelects = function(){
		return $(this).dtSelectArray().toString();
	}
	
	//选中多行的ID数组
	$.fn.dtSelectArray = function(){
		var selectIds=[];
		$(this).find('tbody:eq(0) > tr').each(function(){
			if($(this).attr('objId') && $(this).find('td input[name=checkAll]').attr('checked')){
				selectIds.push($(this).attr('objId'));
			}
		});
		return selectIds;
	}
	
	var oSettingsList = [];
	
	$.fn.myDataTable = function(options){
		
		//初始化参数
		function initSettings(){
			//默认值
			var defaults = {
					singleSelect: true,	
					queryColumns: 'objId',
					pageSize: 10
			};
			options = $.extend(defaults, options);
			
			this.singleSelect = options.singleSelect; //是否为单选，（true:单选，false：多选）
			this.queryColumns = options.queryColumns; //查询字段
			this.hiddenColumns = (options.hiddenColumns==null ? '' : options.hiddenColumns); //隐藏查询字段
			this.pageSize = options.pageSize; //页记录数
			this.searchZone = options.searchZone; //搜索表单域
			this.ajaxUrl = options.ajaxUrl; //资源地址
			this.fnRowCallback = options.fnRowCallback; //画行回调函数
			
		}
		
		//初始化表格
		function _fnInitalise(oSettings){
			//如果是多选，则在thead中添加<checkbox>
			if(!oSettings.singleSelect){
				var chexboxObj = $('<th><input type="checkbox" name="checkAll" /></th>').find('input[type=checkbox]').click(function(){
					//全选
					if(this.checked){
						$(oSettings.nTable).find('tbody tr td input[type=checkbox][name=checkAll]:not(:disabled)').attr('checked', true);
					}else{
						$(oSettings.nTable).find('tbody tr td input[type=checkbox][name=checkAll]:not(:disabled)').attr('checked', false);
					}
				}).parent();
				$(oSettings.nTable).find('thead > tr').prepend(chexboxObj);
			}
			
			//向服务器端获取数据
			_getJsonData(oSettings, null);
			
			//初始化分页信息
			_initPageInfo(oSettings);
			
		}
		
		//刷新当前页面的数据
		this.refreshCurrentPage = function(){
			_dtGotoPage($('#dtPageTable').find('input[id=dtPageIndex]').val());
		}
		
		//重新加载数据
		this.reloadData = function(data){
			_reloadData(data);
		}
		
		//重新加载数据
		function _reloadData(data){
			var oSettings = oSettingsList[0];
			_getJsonData(oSettings, data);
		}
		
		//向服务器端获取数据
		function _getJsonData(oSettings, data){
			if(!data) data={};
			if(!data.pageSize){
				data.pageSize = oSettings.pageSize;
			}
			if(!data.pageIndex){
				data.pageIndex = 1;
			}
			data.queryColumns = oSettings.queryColumns;
			data.hiddenColumns = oSettings.hiddenColumns;
			
			//获取搜索表单的信息
			if(oSettings.searchZone!=null && oSettings.searchZone!=''){
				var formData = formToJsonObject(oSettings.searchZone);
				data = $.extend(data, formData);
			}
			
			$.getJSON(oSettings.ajaxUrl,data,function(json){
				//画行
				_drawRow(oSettings, json.pageData);
				
				//设置分页信息
				_setPageInfo(json.totalRecord, json.pageIndex, json.pageSize, json.pageCount);
			});
		}
		
		//画行
		function _drawRow(oSettings, rowData){
			//获取表头对象列表
			var thList = $(oSettings.nTable).find('thead th');
			
			$(oSettings.nTable).find('tbody').empty(); //清空tbody中的数据
			
			$.each(rowData,function(index, data){
				var trStr = '<tr objId="'+data.objId+'">';
				if(!oSettings.singleSelect){
					trStr += '<td align="center"><input type="checkbox" name="checkAll" /></td>'
				}
				var queryColumnArr = oSettings.queryColumns.split(',');
				for(var i=0; i<queryColumnArr.length; i++){
					var queryColumn = queryColumnArr[i];
					var columnValue = (data[queryColumn]==null ? '' : data[queryColumn]);
					
					//处理时间
					if(columnValue!='' && $(thList[i+1]).hasClass('time')){
						//columnValue = _date2str(columnValue);
					}
					trStr += '<td>'+columnValue+'</td>';
				}
				trStr += '</tr>';
				
				var nRow = $(trStr);
				
				//调用回调方法
				if(typeof oSettings.fnRowCallback == 'function'){
					nRow = oSettings.fnRowCallback(nRow, data);
				}
				
				$(oSettings.nTable).find('tbody').append(nRow);
			});
		}
		
		/*************************分页相关开始*********************************/
		//初始化分页信息
		function _initPageInfo(oSettings){
			var pageTableStr = '<table id="dtPageTable" width="100%" style="font-size:13px;"><tbody><tr><td align="center" class="pn-sp">'+
					'<input type="hidden" id="dtTotalRecord" value="" />'+
					'<input type="hidden" id="dtPageIndex" value="" />'+
					'<input type="hidden" id="dtPageCount" value="" />'+
					'<input type="hidden" id="dtPageSize" value="" />'+
					'共<span id="dtTotalRecordShow"></span>条&nbsp;'+
					'每页<input id="dtPageSizeShow" type="text" onfocus="this.select();" style="width:30px" maxlength="3" />条&nbsp;'+
					'<input id="dtGotoFirstPageBut" type="button" value="首页" class="first-page">&nbsp;'+
					'<input id="dtGotoPrePageBut" type="button" value="上一页" class="pre-page">&nbsp;'+
					'<input id="dtGotoNextPageBut" type="button" value="下一页" class="next-page">&nbsp;'+
					'<input id="dtGotoLastPageBut" type="button" value="&nbsp;尾页" class="last-page">&nbsp;'+
					'当前<span id="dtPageIndexShow"></span>/<span id="dtPageCountShow"></span>页 &nbsp;'+
					'转到第<input type="text" id="dtGotoPageNum" onfocus="this.select();" style="width:50px" />页&nbsp;'+
					'<input type="button" id="dtRefreshPageDataBut" value="刷新" class="go" />'+
					'</td></tr></tbody></table>';
			$(oSettings.nTable).after(pageTableStr);
			
			$('#dtPageTable').find('input[id=dtGotoFirstPageBut]').click(function(){ _dtGotoPage('first'); });
			$('#dtPageTable').find('input[id=dtGotoPrePageBut]').click(function(){ _dtGotoPage('pre'); });
			$('#dtPageTable').find('input[id=dtGotoNextPageBut]').click(function(){ _dtGotoPage('next'); });
			$('#dtPageTable').find('input[id=dtGotoLastPageBut]').click(function(){ _dtGotoPage('last'); });
			$('#dtPageTable').find('input[id=dtRefreshPageDataBut]').click(function(){
				var pageNumStr = $('#dtPageTable').find('input[id=dtGotoPageNum]').val();
				if(pageNumStr==null || pageNumStr==''){
					pageNumStr = $('#dtPageTable').find('input[id=dtPageIndex]').val();
				}
				var pageSizeStr = $('#dtPageTable').find('input[id=dtPageSizeShow]').val();
				if(pageSizeStr!=null && pageSizeStr!=''){
					$('#dtPageTable').find('input[id=dtPageSize]').val(pageSizeStr);
				}
				_dtGotoPage(pageNumStr);
			});
			$('#dtPageTable').find('input[id=dtPageSizeShow]').keyup(function(event){
				var pageSizeStr = $(this).val();
				if(event.keyCode==13){
					if(pageSizeStr!=null && pageSizeStr!='' && !isNaN(pageSizeStr)){
						$('#dtPageTable').find('input[id=dtPageSize]').val(pageSizeStr);
					}
					$('#dtPageTable').find('input[id=dtRefreshPageDataBut]').click();
				}else{
					var currChar = pageSizeStr.charAt(pageSizeStr.length-1);
					if(pageSizeStr == '0'){
						$(this).val('');
					}else if("0123456789".indexOf(currChar)<0){
						$(this).val(pageSizeStr.substring(0, pageSizeStr.length-1));
					}
				}
			});
			$('#dtPageTable').find('input[id=dtGotoPageNum]').keyup(function(event){
				var pageNumStr = $(this).val();
				if(event.keyCode==13){
					_dtGotoPage(pageNumStr);
				}else{
					var currChar = pageNumStr.charAt(pageNumStr.length-1);
					if(pageNumStr == '0'){
						$(this).val('');
					}else if("0123456789".indexOf(currChar)<0){
						$(this).val(pageNumStr.substring(0, pageNumStr.length-1));
					}
				}
			});
		}
		//设置分页信息
		function _setPageInfo(totalRecord, pageIndex, pageSize, pageCount){
			var pageTable = $('#dtPageTable');
			$(pageTable).find('input[id=dtTotalRecord]').val(totalRecord);
			$(pageTable).find('input[id=dtPageIndex]').val(pageIndex);
			$(pageTable).find('input[id=dtPageCount]').val(pageCount);
			$(pageTable).find('input[id=dtPageSize]').val(pageSize);
			$(pageTable).find('span[id=dtTotalRecordShow]').html(totalRecord);
			$(pageTable).find('span[id=dtPageIndexShow]').html(pageIndex);
			$(pageTable).find('span[id=dtPageCountShow]').html(pageCount);
			$(pageTable).find('input[id=dtPageSizeShow]').val(pageSize);
			$(pageTable).find('input[id=dtGotoPageNum]').val('');
		}
		//跳转到指定页面
		function _dtGotoPage(page){
			var pageTable = $('#dtPageTable');
			var pageIndex = Number($(pageTable).find('input[id=dtPageIndex]').val());
			var pageCount = Number($(pageTable).find('input[id=dtPageCount]').val());
			var pageSize = Number($(pageTable).find('input[id=dtPageSize]').val());
			var gotoPage = 1;
			if('first' == page){
				if(pageIndex <=1 ){return ;}
				gotoPage = 1;
			}else if('pre' == page){
				if(pageIndex <= 1){return ;}
				gotoPage = pageIndex-1;
			}else if('next' == page){
				if(pageIndex >= pageCount){return ;}
				gotoPage = pageIndex+1;
			}else if('last' == page){
				if(pageIndex >= pageCount){return ;}
				gotoPage = pageCount;
			}else{
				if(isNaN(page)){
					return ;
				}else if(Number(page) <= 0){
					gotoPage = 1;
				}else if(Number(page) >= pageCount){
					gotoPage = pageCount;
				}else{
					gotoPage = page;
				}
			}
			
			//重新加载数据
			var data = {};
			data.pageIndex = gotoPage;
			data.pageSize = pageSize;
			_reloadData(data);
		}
		/*************************分页相关结束*********************************/
		
		/*************************内部公用方法开始*********************************/
		//把数字日期转换为yyyy-MM-dd HH:mm:ss格式的字符串
		function _date2str(ms){
			var date = new Date(ms);
			var month = date.getMonth()+1>9 ? date.getMonth()+1 : '0'+(date.getMonth()+1);
			var day = date.getDate()>9 ? date.getDate() : '0'+date.getDate();
			var hours = date.getHours()>9 ? date.getHours() : '0'+date.getHours();
			var minutes = date.getMinutes()>9 ? date.getMinutes() : '0'+date.getMinutes();
			var seconds = date.getSeconds()>9 ? date.getSeconds() : '0'+date.getSeconds();
			return date.getFullYear()+'-'+month+'-'+day+' '+hours+':'+minutes+':'+seconds;
		}
		/*************************内部公用方法结束*********************************/
		
		
		return this.each(function(){
			
			var oSettings = new initSettings();
			
			oSettings.nTable = this;
			oSettings.nTableId = $(this).attr('id');
			
			oSettingsList.push(oSettings);
			
			_fnInitalise(oSettings);
		});
				
	}
	
})(jQuery);