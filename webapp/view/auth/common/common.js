
var common = {};

//�ر�msgbox������
common.closeMsgbox = function(){
	$('a.jMsgbox-closeWrap').click();
}

//msgbox ��ʾ��
common.tipMsgbox = function(content){
	new $.msgbox({
		autoClose: 2,
		type: 'alert',
		content: content
	}).show();
}

//msgbox �����
common.alertMsgbox = function(content){
	new $.msgbox({
		type: 'alert',
		content: content
	}).show();
}

//��ת����Ʒ����ҳ��
common.toGoodsDetail = function(goodsId){
	window.open('/GoodsController.do?method=toShowGoodsDetailView&objId='+goodsId);
}

//��ת���������ҳ��
common.toArticleDetail = function(articleId){
	var visitType = '&s=0'; //�Ƿ���ʾ�̬ҳ��(1:���ʾ�̬ҳ�棻0:���ʶ�̬ҳ��)
	window.open('/ArticleController.do?method=toShowArticleDetailView&objId='+articleId+visitType);
}