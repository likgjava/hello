/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config ){
	// Define changes to default configuration here. For example:
	//config.language = 'fr'; //User Interface Languages
	//config.uiColor = '#AADC6E'; //UI Color Picker
	config.skin='v2'; //Skins
	
	//config.pasteFromWordRemoveStyles = true;
	//设置图片文件上传地
	config.filebrowserImageUploadUrl = "/AttachmentController.do?method=uploadCKEditorImg";
	//预览图片框的显示内容
	config.image_previewText = ' ';
	
	
	//自定义工具栏
	/**
	config.toolbar = 'MyToolbar';
	config.toolbar_MyToolbar =
	[
		{ name: 'document', items : ['Source','Preview' ] },
		{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] }, //'-'：表示分割线
		{ name: 'editing', items : [ 'Find','Replace','-','Scayt' ] },
		{ name: 'insert', items : [ 'Image','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] }, //,'Flash'
		'/', //换行符
		{ name: 'styles', items : [ 'Styles','Format' ] },
		{ name: 'basicstyles', items : [ 'Bold','Italic','Strike','-','RemoveFormat' ] },
		{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote' ] },
		{ name: 'links', items : [ 'Link','Unlink' ] },
		{ name: 'tools', items : [ 'Maximize' ] }
	];
	*/

};

//		{ name: 'document', items : [ 'NewPage','Preview' ] },
//		{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] }, //'-'：表示分割线
//		{ name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','Scayt' ] },
//		{ name: 'insert', items : [ 'Image','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] }, //,'Flash'
//		'/', //换行符
//		{ name: 'styles', items : [ 'Styles','Format' ] },
//		{ name: 'basicstyles', items : [ 'Bold','Italic','Strike','-','RemoveFormat' ] },
//		{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote' ] },
//		{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
//		{ name: 'tools', items : [ 'Maximize','-','About' ] }