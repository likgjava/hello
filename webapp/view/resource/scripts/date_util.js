
//����������ת��Ϊyyyy-MM-dd HH:mm:ss��ʽ���ַ���
function date2str(ms){
	var date = new Date(ms);
	return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
}