package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ReadTxt {

	public static void main(String[] args){
		String siteId = "92b51aea-b546-4b74-9ac3-d7e6a3878c3f";
		String siteFileId = "042bfe0e45de8e6ceb1a56c7383baa2daa3823a758d105a01696f781d47e8508";
		String dirName = "D:/test";
		
		String filepath = dirName + "/" + siteId + "/" + siteFileId + ".txt";
		String txt = readTxtFile(filepath);
		
		if(txt.equals("")){
			return;
		}
		String type = txt.substring(txt.indexOf("type:")+5, txt.indexOf("check"));
		System.out.println("文件类型为：" + type);
		int cnt = Integer.valueOf(txt.substring(txt.indexOf("Hit Cnt:")+8, txt.indexOf("Hit position")));
		System.out.println("上下文个数为：" + cnt);
		String arr[] = txt.split("Hit position");
		/*Pattern p = Pattern.compile("^[\u0000-\uffff]{2}:$");
	    Matcher m = p.matcher(arr[1]);
	    while(m.find()){
	    	System.out.println(m.group());
	    }
	    */
		
		int count = arr.length -1;
		Map<String,Integer> keyCnt = new HashMap<String, Integer>();
		//System.out.println(txt.indexOf(","));
		//System.out.println(txt.substring(txt.indexOf(",") + 1, txt.indexOf("<") -1));
		for(int i=1; i<arr.length; i++){
			
			String strI = arr[i];
			String text = strI.substring(strI.indexOf(":") + 1);
			String kwd = arr[1].substring(arr[i].indexOf(",") +1, arr[i].indexOf(":"));
			if(keyCnt.containsKey(kwd)){
				int t = keyCnt.get(kwd) + 1;
				keyCnt.put(kwd, t);
			}
			else{
				keyCnt.put(kwd, 1);
			}
			System.out.println();
			System.out.println("第" + i + "条:" + "关键字是：" + kwd);
			System.out.println("上下文是:" +text);
			
		}
		int temp = 0;
		for(String str : keyCnt.keySet()){
			System.out.println(str + ":" + keyCnt.get(str));
			temp += keyCnt.get(str);
		}
		System.out.println("keyCnt.size():" + temp);
		System.out.println("count:" +count);
	}
	
	public static String readTxtFile(String filepath){
		String str = "";
		
		String encoding = "utf-8";
		
		File file = new File(filepath);
		if(file.exists() && file.isFile()){
			try {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufreader = new BufferedReader(read);
				String lineTxt = null;
                while((lineTxt = bufreader.readLine()) != null){
                	str += lineTxt;
                  //  System.out.println(lineTxt);
                }
                read.close();
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("文件访问错误");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("读取内容出错");
				e.printStackTrace();
			}
		}
		else{
			System.out.println("文件不存在");
		}
		//System.out.println(str);
		return str;
	}
}
