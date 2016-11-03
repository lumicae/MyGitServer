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
		dataMap.put("xytitle", "�Ծ�");
		int index = 1;
		
		//ѡ����
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();//��Ŀ
		
		index = 1;
		for(int i=0;i<5;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("xzn", index + ".");
			map.put("xztest", "(  )����ϵͳ������һ̨������ͬʱ���Ӷ�̨�նˣ�����û�����ͨ������"
					+ "���ն�ͬʱ������ʹ�ü������");
			map.put("ans1",  "A" + index);
			map.put("ans2",  "B" + index);
			map.put("ans3",  "C" + index);
			map.put("ans4", "D" + index);
			list1.add(map);
			
			index ++;
		}
		dataMap.put("table1",  list1);
		
		//�����
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		
		index = 1;
		for(int i=0; i<5; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tkn", index+ ".");
			map.put("tktest", "����ϵͳ�Ǽ����ϵͳ�е�һ����������ϵͳ���������������������Ϳ��Ƽ����"
					+ "ϵͳ�еġ�������������Դ����������������");
			list2.add(map);
			
			index++;
		}
		dataMap.put("table2",  list2);
		
		//�ж���
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
		
		index = 1;
		for(int i=0; i<5; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pdn",  index + ".");
			map.put("pdtest", "�����ͷ���ǽ���ڲ������ⲿ���ĸ���㣬 ���ż��Ӻ͸���Ӧ�ò�ͨ����"
					+ "�����ã� ͬʱҲ����Ϲ������Ĺ��ܡ�");
			list3.add(map);
			
			index ++;
		}
		dataMap.put("table3", list3);
	
		//�����
		List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
		
		index = 1;
		for(int i=0; i<5; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("jdn",  index + ".");
			map.put("jdtest",  "˵����ҵ���ȣ� �м����Ⱥͽ��̵��ȵ����𣬲�������������Ӧ��"
					+ "��һ�����ȳ�����");
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
