package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class TestExport {
	public static void main(String[] args){
		TestExport te = new TestExport();
		te.genListReport();
	}
	
	public void genListReport(){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> table0 = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("a", "ceshiA");
		
		List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("b", "ceshiB");
		table1.add(map2);
		
		List<Map<String, Object>> table2 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("c", "ceshiC");
		table2.add(map3);
		
		map1.put("table2", table2);
		map1.put("table1", table1);
		
		table0.add(map1);
		
		dataMap.put("table0", table0);
		
		LoadWordTemplate lt = new LoadWordTemplate();
		lt.createListWord(dataMap, "D:/javaDoc/testFile.doc");
		
	}
	
	public void genReport(HttpServletResponse response){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("xytitle", "试卷");
		int index = 1;
		
		//选择题
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();//题目
		
		index = 1;
		for(int i=0;i<5;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("xzn", index + ".");
			map.put("xztest", "(  )操作系统允许在一台主机上同时连接多台终端，多个用户可疑通过各自"
					+ "的终端同时交互地使用计算机。");
			map.put("ans1",  "A" + index);
			map.put("ans2",  "B" + index);
			map.put("ans3",  "C" + index);
			map.put("ans4", "D" + index);
			list1.add(map);
			
			index ++;
		}
		dataMap.put("table1",  list1);
		
		//填空题
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		
		index = 1;
		for(int i=0; i<5; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tkn", index+ ".");
			map.put("tktest", "操作系统是计算机系统中的一个————系统软件—————，它管理和控制计算机"
					+ "系统中的——————资源————————");
			list2.add(map);
			
			index++;
		}
		dataMap.put("table2",  list2);
		
		//判断题
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
		
		index = 1;
		for(int i=0; i<5; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pdn",  index + ".");
			map.put("pdtest", "复合型防火墙是内部网与外部网的隔离点， 起着监视和隔绝应用层通信流"
					+ "的作用， 同时也常结合过滤器的功能。");
			list3.add(map);
			
			index ++;
		}
		dataMap.put("table3", list3);
	
		//简答题
		List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
		
		index = 1;
		for(int i=0; i<5; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("jdn",  index + ".");
			map.put("jdtest",  "说明作业调度， 中级调度和进程调度的区别，并分析下属问题应有"
					+ "哪一级调度程序负责。");
			list4.add(map);
			
			index ++;
		}
		dataMap.put("table4", list4);
		
		LoadWordTemplate lt = new LoadWordTemplate();
		try {
			
			lt.createWordHttp(dataMap, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
