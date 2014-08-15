
//把数字日期转换为yyyy-MM-dd HH:mm:ss格式的字符串
function date2str(ms){
	var date = new Date(ms);
	return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
}