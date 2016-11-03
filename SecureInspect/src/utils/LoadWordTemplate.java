package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class LoadWordTemplate {

	private Configuration configuration = null;
	
	public LoadWordTemplate(){
		configuration = new Configuration();
		configuration.setDefaultEncoding("UTF-8");
	}
	
	
	//将生成的word返回给浏览器
	public void createWordHttp(Map<String, Object> dataMap, OutputStream os){
		configuration.setClassForTemplateLoading(this.getClass(), "");
		Template t = null;
		
		try {
			t = configuration.getTemplate("siteReportTemplat.ftl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Writer out = null;
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));   
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			t.process(dataMap, out);
			out.flush(); 
			out.close();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//测试使用，未完成测试
	public void createListWord(Map<String, Object> dataMap, String outPath){
		configuration.setClassForTemplateLoading(this.getClass(), "");
		Template t = null;
		
		try {
			t = configuration.getTemplate("test.ftl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File outFile = new File(outPath);
		Writer out = null;
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));   
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			t.process(dataMap, out);
			out.flush(); 
			out.close();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
