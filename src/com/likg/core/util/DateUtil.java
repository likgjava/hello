package com.likg.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** 
  *  Comments: <strong>DateUtil</strong>            		<br/>
  *	 增加了静态公有属性					<br/>												
  *  CopyRright (c)2008-xxxx:   珠海政采软件技术有限公司    		<br/>
  *  Project:   srplatform                    					<br/>           
  *  Module ID: 权限平台     		<br/>
  *  Create Date：2009-12-13 下午04:18:45 by 未知    					<br/>                                 
  *  Modified Date:  2009-12-13 下午04:18:45 by wangsw                   <br/>               
  *      
  *  @since 0.4
  *  @version: 0.5 
  */ 
public final class DateUtil{
	
	/** 默认的年月日 */
	public static final String defaultPattern = "yyyy-MM-dd";
	
	/** hour12HMSPattern年月日 时分秒 12小时制*/
	public static final String hour12HMSPattern = "yyyy-MM-dd hh:mm:ss";
	
	/** hour12HMPattern年月日 时分 12小时制*/
	public static final String hour12HMPattern = "yyyy-MM-dd hh:mm";
	
	/** hour12HPattern年月日 时 12小时制*/
	public static final String hour12HPattern = "yyyy-MM-dd hh";
	
	/** hour24HMSPattern年月日 时分秒 24小时制 */
	public static final String hour24HMSPattern = "yyyy-MM-dd HH:mm:ss";
	
	/** hour24HMPattern年月日 时分 24小时制*/
	public static final String hour24HMPattern = "yyyy-MM-dd HH:mm";
	
	/** hour24HPattern年月日 时 24小时制*/
	public static final String hour24HPattern = "yyyy-MM-dd HH";

	/** 
	 * Description :  计算时间差，  例如返回  2天1小时4分20秒
	 * Create Date: 2010-1-17上午01:46:22 by wangsw  Modified Date: 2010-1-17上午01:46:22 by wangsw
	 * @param  开始时间 
	 * @return  例如返回  2天1小时4分20秒 的字符串
	 * @Exception   
	 */
	public static String daysBetween(Date startDate, Date endDate) {     
		float d = endDate.getTime() - startDate.getTime();
		float dd = d / 86400000f;
		int ddd = (int)dd;
		
		float hh = (dd - ddd) * 24;
		int hhh = (int)hh;
		
		float mm = (hh - hhh) * 60;
		int mmm = (int)mm;
		
		float ss = (mm - mmm) * 60;
		int sss = (int)ss;
	    return ddd + "天" + hhh + "小时" + mmm + "分" + sss + "秒";
	}  
	
	/**
	 * 返回预设Format的当前日期字符串
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	public static String getYestoday() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.add(Calendar.DATE, -1);
		return format(cal1.getTime());
	}

	public static String getTheDayBeforeYestoday() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.add(Calendar.DATE, -2);
		return format(cal1.getTime());
	}

	public static String getPreviousDay3() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.add(Calendar.DATE, -3);
		return format(cal1.getTime());
	}

	public static String getPreviousDay4() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.add(Calendar.DATE, -4);
		return format(cal1.getTime());
	}

	/**
	 * 用预设Format格式化Date成字符串
	 */
	public static String format(Date date) {
		return format(date, defaultPattern);
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(Date date, String pattern) {
		String returnValue = "";

		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}

		return (returnValue);
	}

	/**
	 * 使用预设格式将字符串转为Date
	 */
	public static Date parse(String strDate) throws ParseException {
		return parse(strDate, defaultPattern);
	}

	/** 
	 * Description :   使用参数Format将字符串转为Date
	 * Create Date: 2009-12-13下午04:27:49 by wangsw  Modified Date: 2009-12-13下午04:27:49 by wangsw
	 * @param  字符、 格式参考<code>DateUtil 的静态常量</code>  
	 * @return  Date
	 * @Exception   
	 */
	public static Date parse(String strDate, String pattern)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.parse(strDate);
	}

	/**
	 * 在日期上增加数个整月
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}
	
	/**
	 * 在日期上增加日
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}

	public static String formatDateTime(Date date) {
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return outFormat.format(date);
	}

	@SuppressWarnings("static-access")
	public static String getEndOfMonth(String year, String month) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.YEAR, Integer.parseInt(year));
		cal.set(cal.MONTH, Integer.parseInt(month) - 1);
		return cal.getActualMaximum(cal.DAY_OF_MONTH) + "";
	}

	public static String addDays(String sdate, int n) throws ParseException
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(parse(sdate, defaultPattern));
		cal1.add(Calendar.DATE, n);
		return format(cal1.getTime());

	}

	public static String getFirstOfMonth() throws ParseException {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.set(5, 1);
		return format(cal1.getTime());

	}

	public static String getFirstOfMonth(String sDate) throws ParseException {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(parse(sDate, defaultPattern));
		cal1.set(5, 1);
		return format(cal1.getTime());

	}

	/**
	 * 获取年
	 * 
	 * @param sdate
	 * @return String
	 */
	public static String getYear(String sdate) {
		String[] date = sdate.split("-");
		return date[0];
	}

	/**
	 * 获取月
	 * 
	 * @param sdate
	 * @return String
	 */
	public static String getMonth(String sdate) {
		String[] date = sdate.split("-");
		return date[1];
	}

	public static String getCurrentYear() {
		Calendar cale = Calendar.getInstance();
		return Integer.toString(cale.get(Calendar.YEAR));
	}

	public static String getCurrentMonth() {
		Calendar cale = Calendar.getInstance();
		int month = cale.get(Calendar.MONTH);
		month++;
		String sMonth = Integer.toString(month);
		if (month < 10)
			sMonth = "0" + month;
		return sMonth;
	}

	/**
	 * 获取天
	 * 
	 * @param sdate
	 * @return String
	 */
	public static String getDay(String sdate) {
		String[] date = sdate.split("-");
		return date[2];
	}

	public static String getFullDate(String date) {
		if (date != null && date.length() == 1)
			return "0" + date;
		return date;
	}

	public static String getSimpleDateString(String sdate) {
		return sdate.replace("-", "");
	}
	
    //把日期从字符弄转成日期型
    public static Date convertStringToDate(String pattern, String strDate)
      throws ParseException {
        Date aDate = null;
        aDate = parse(strDate, pattern);
        return aDate;
    }
    
    //根据指定格式得到当前日期的字符串
    public static String getTodayDate(String aMask){
    	Date date = new Date();
    	return getDateTime(aMask,date);
    }
    
    //根据指定格式得到指定日期的字符串
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        df = new SimpleDateFormat(aMask);
        returnValue = df.format(aDate);
        return (returnValue);
    }
    
    /**
	 *  
	 * Description :  返回年 
	 * Create Date: 2009-12-30下午03:59:47 by lic  Modified Date: 2009-12-30下午03:59:47 by lic
	 * @param   
	 * @return  返回年 
	 * @Exception
	 */
	public static int getYear() { 
	  Calendar c = Calendar.getInstance(); 
	  int yy = c.get(Calendar.YEAR); 
	  return yy; 
	} 

	/**
	 *  
	 * Description :  返回月 
	 * Create Date: 2009-12-30下午03:59:47 by lic  Modified Date: 2009-12-30下午03:59:47 by lic
	 * @param   
	 * @return  返回月 
	 * @Exception
	 */
	public static int getMonth() { 
	  Calendar c = Calendar.getInstance(); 
	  int month = c.get(Calendar.MONTH); 
	  return month+1; 
	} 
 
	/**
	 *  
	 * Description :  返回日 
	 * Create Date: 2009-12-30下午03:59:47 by lic  Modified Date: 2009-12-30下午03:59:47 by lic
	 * @param   
	 * @return  返回日 
	 * @Exception
	 */
	public static int getDate() { 
	  Calendar c = Calendar.getInstance(); 
	  int date = c.get(Calendar.DATE); 
	  return date; 
	} 
	
	/**
	 *  
	 * Description :  返回时
	 * Create Date: 2009-12-30下午03:59:47 by lic  Modified Date: 2009-12-30下午03:59:47 by lic
	 * @param   
	 * @return  返回时 
	 * @Exception
	 */
	public static int getHour() { 
	  Calendar c = Calendar.getInstance(); 
	  int hour = c.get(Calendar.HOUR_OF_DAY); 
	  return hour; 
	} 

	/**
	 *  
	 * Description :  返回秒 
	 * Create Date: 2009-12-30下午03:59:47 by lic  Modified Date: 2009-12-30下午03:59:47 by lic
	 * @param   
	 * @return  返回秒
	 * @Exception
	 */
	public static int getSecond() { 
	  Calendar c = Calendar.getInstance(); 
	  int second = c.get(Calendar.SECOND); 
	  return second; 
	} 

	/**
	 *  
	 * Description :  返回分
	 * Create Date: 2009-12-30下午03:59:47 by lic  Modified Date: 2009-12-30下午03:59:47 by lic
	 * @param   
	 * @return  返回分
	 * @Exception
	 */
	public static int getMinute() { 
	  Calendar c = Calendar.getInstance(); 
	  int minute = c.get(Calendar.MINUTE); 
	  return minute; 
	} 
    
	/**
	 * 
	 * Description :  当前日期及时间"####-##-## ##:##:##"
	 * Create Date: 2010-1-4上午11:48:34 by lic  Modified Date: 2010-1-4上午11:48:34 by lic
	 * @param   
	 * @return  
	 * @Exception
	 */
	public static String getCurDateTime() {
	    return getCurDate() + " " + getCurTime();
	}
	
	/**
	 * 
	 * Description :  得到当前日期"####-##-##"
	 * Create Date: 2010-1-4上午11:48:05 by lic  Modified Date: 2010-1-4上午11:48:05 by lic
	 * @param   
	 * @return  当前日期"####-##-##"
	 * @Exception
	 */
	public static String getCurDate() {
		String fullDate = getCurYearMonth();
	    int temp = getDate();
	    if(temp < 10){
	      fullDate += "-0" + temp;
	    }else{
	      fullDate += "-" + temp;
	    }
	    return fullDate;
	}
	
	/**
	 * 
	 * Description :  得到当前日期"####-##"
	 * Create Date: 2010-1-4上午11:50:12 by lic  Modified Date: 2010-1-4上午11:50:12 by lic
	 * @param   
	 * @return 当前日期"####-##" 
	 * @Exception
	 */
	public static String getCurYearMonth() {
	    String fullDate = String.valueOf(getYear());
	    int temp = getMonth();
	    if(temp < 10){
	      fullDate += "-0" + temp;
	    }else{
	      fullDate += "-" + temp;
	    }
	    return fullDate;
	}

	/**
	 * 
	 * Description :  得到当前时间"##:##:##"
	 * Create Date: 2010-1-4上午11:50:47 by lic  Modified Date: 2010-1-4上午11:50:47 by lic
	 * @param   
	 * @return 当前时间"##:##:##" 
	 * @Exception
	 */
	public static String getCurTime() {
		String time = getCurHourMinute();
		int temp = getSecond();
		if(temp < 10){
			time += ":0" + temp;
		}else{
			time += ":" + temp;
		}
		return time;
	}
	  
	/**
	 * 
	 * Description :  得到当前时间"##:##"
	 * Create Date: 2010-1-4上午11:51:08 by lic  Modified Date: 2010-1-4上午11:51:08 by lic
	 * @param   
	 * @return  当前时间"##:##"
	 * @Exception
	 */
	public static String getCurHourMinute() {
		String time;
		int temp = getHour();
		if(temp < 10){
			time = "0" + temp + ":";
		}else{
			time = temp + ":";
		}
		temp = getMinute();
		if(temp < 10){
			time += "0" + temp;
		}else{
			time += temp;
		}
		return time;
	}
	
	/** 
	 * Description :  处理日期格式转化
	 * Create Date: 2010-1-10下午10:32:41 by wangsw  Modified Date: 2010-1-10下午10:32:41 by wangsw
	 * @param   日期对象
	 * @return  日期的字符串格式
	 * @Exception   
	 */
	public static String DateFormat(Date date) {
		Calendar c=Calendar.getInstance();
		c.setTime(date); 
		c.get(java.util.Calendar.HOUR_OF_DAY);
		String pattern="yyyy-MM-dd";
		if(c.get(java.util.Calendar.HOUR_OF_DAY)!=0){
			pattern="yyyy-MM-dd hh";
		}else if(c.get(java.util.Calendar.MINUTE)!=0){
			pattern="yyyy-MM-dd hh:mm";		
		}else if(c.get(java.util.Calendar.SECOND)!=0){
			pattern="yyyy-MM-dd hh:mm:ss";
		}
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static void main(String[] args){
		//Calendar c = Calendar.getInstance(); 
		//int month = c.get(Calendar.MONTH); 
		//System.out.println(month);
		System.out.println(getMonth());
//		Date d=new Date();   
//        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
//        String beforeDate = df.format(new Date(d.getTime() - 10 * 24 * 60 * 60 * 1000));   
//		String a = "select count(*), LGN_IN_TIME from  BASE_LOGIN_LOGS where usr_id='8a808083204329sr01204330167b0005' where LGN_IN_TIME>to_date('" + beforeDate + "'," + "'yyyy-mm-dd'" + ") group by LGN_IN_TIME";
//		System.out.print(a);
	}
}