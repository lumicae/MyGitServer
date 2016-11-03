package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConverStringUtil {
	/**
	 * ת���б�Ϊ�ַ���
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
	 * sqlʹ��id IN�������ʱ��Ҫdecodeת��Ϊ���ֽ�������(��δʹ��)
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
	 * ��long��������תΪ�ַ���
	 * @param create_time
	 * @return
	 */
	public static String convertLongTime(long create_time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date(create_time);
		
		return formatter.format(date);
	}
	
	/**
	 * ��flotaת���ɴ洢��ʽ��ʾ
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
	 * �ַ���ת��Ϊ���ڸ�ʽ
	 * @param strFormat ģ��
	 * @param dateValue ��Ҫת�����ַ���
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
	 * ����ת��Ϊ�ַ���
	 * @param fromatString ��ʽģ��
	 * @param myDate ����
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
