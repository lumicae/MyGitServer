package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConverStringUtil {
	/**
	 * 转化列表为字符串
	 * @param idList
	 * @return
	 */
	public static String generateSequence(List<String> idList) {
		
		if (idList.size() == 0) {
			return "''";
		}
		
		String ids = "";
		
		for (String id : idList) {
			ids += "'" + id + "',";
		}
		
		ids = ids.substring(0, ids.length()-1);
		
		return ids;
	}
	
	/**
	 * sql使用id IN语句排序时需要decode转换为数字进行排序(暂未使用)
	 * @param idList
	 * @return
	 */
	public static String generateDecodeNumber(List<String> idList) {
		
		if (idList.size() == 0) {
			return "''";
		}
		
		String ids = "";
		
		for (int i=0;i<idList.size();i++) {
			ids += "'" + idList.get(i) + "'," + (i+1) + ",";
		}
		
		ids = ids.substring(0, ids.length()-1);
		
		return ids;
	}
	
	/**
	 * 将long日期类型转为字符串
	 * @param create_time
	 * @return
	 */
	public static String convertLongTime(long create_time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date(create_time);
		
		return formatter.format(date);
	}
	
	/**
	 * 将flota转换成存储格式显示
	 * @param size
	 * @return
	 */
	public static String parseSize(double size) {
		
		String result = "0 B";
		
		if (size < 1024) {
			result = String.format("%f B", size);
		} else if (size < 1048576) {
			result = String.format("%.2f KB", size/1024);
		} else if (size < 1073741824) {
			result = String.format("%.2f MB", size/1048576);
		} else {
			result = String.format("%.2f GB", size/1073741824);
		}
				
		return result;
	}
	
	/**
	 * 字符串转换为日期格式
	 * @param strFormat 模版
	 * @param dateValue 需要转换的字符串
	 * @return
	 */
	public static Date parseDate(String strFormat, String dateValue) {		
		
		if (dateValue == null)	{
			return null;
		}		
					
		if (strFormat == null)	{
			strFormat = "yyyy-MM-dd";
		}		
			
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;
		
		try {	
			
			newDate = dateFormat.parse(dateValue);
			
		} catch (ParseException pe) {
			System.out.println(pe);
			
			newDate = null;		
		}
		
		return newDate;	
		
	}
	
	/**
	 * 日期转换为字符串
	 * @param fromatString 格式模版
	 * @param myDate 日期
	 * @return
	 */
	public static String dateFormat(String fromatString, Date myDate){
		 
		if (myDate == null)	{
			return null;
		}		
					
		if (fromatString == null)	{
			fromatString = "yyyy-MM-dd";
		}	
		
	     SimpleDateFormat myFormat = new SimpleDateFormat(fromatString); 
	     
	     return myFormat .format(myDate); 
	}
}
