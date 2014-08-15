
var common = {};

//关闭msgbox弹出框
common.closeMsgbox = function(){
	$('a.jMsgbox-closeWrap').click();
}

//msgbox 提示框
common.tipMsgbox = function(content){
	new $.msgbox({
		autoClose: 2,
		type: 'alert',
		content: content
	}).show();
}

//msgbox 警告框
common.alertMsgbox = function(content){
	new $.msgbox({
		type: 'alert',
		content: content
	}).show();
}

//跳转到商品详情页面
common.toGoodsDetail = function(goodsId){
	window.open('/GoodsController.do?method=toShowGoodsDetailView&objId='+goodsId);
}

//跳转到稿件详情页面
common.toArticleDetail = function(articleId){
	var visitType = '&s=0'; //是否访问静态页面(1:访问静态页面；0:访问动态页面)
	window.open('/ArticleController.do?method=toShowArticleDetailView&objId='+articleId+visitType);
}