package mvc.weixin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import logic.preview.PreviewService;
import mvc.site.AdvSearchPara;
import mvc.site.BriefSearchPara;
import mvc.site.FilePrePara;
import mvc.site.KeyContext;
import mvc.site.PagePrePara;
import mvc.site.TaskPrePara;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import utils.PageView;
import utils.TestExport;

@Controller
public class WeixinController {
	@Autowired
	@Qualifier("previewService")
	private PreviewService previewService;

	@RequestMapping("/weixinHome")
	public ModelAndView siteHome(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("weixinHome");
		return mav;
	}
	
	@RequestMapping("/firstLoadWeixin")
	public ModelAndView firstLoadWeixin(@Valid BriefSearchPara briefSearch){
		ModelAndView mav = new ModelAndView();
		briefSearch.setCurPage("1");
		briefSearch.setPageSize("10");
		briefSearch.setTaskType("weixin");
		PageView<TaskPrePara> pageView = previewService.briefSearchForTask(briefSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("weixinList");
		return mav;
	}
	
	@RequestMapping("/firstLoadPage")
	public ModelAndView firstLoadPage(@Valid BriefSearchPara briefSearch){
		ModelAndView mav = new ModelAndView();
		briefSearch.setCurPage("1");
		briefSearch.setPageSize("10");
		briefSearch.setTaskType("weixin");
		PageView<PagePrePara> pageView = previewService.briefSearchForPage(briefSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("pageList");
		return mav;
	}
	
	@RequestMapping("/firstLoadFile")
	public ModelAndView firstLoadFile(@Valid BriefSearchPara briefSearch){
		ModelAndView mav = new ModelAndView();
		briefSearch.setCurPage("1");
		briefSearch.setPageSize("10");
		briefSearch.setTaskType("weixin");
		PageView<FilePrePara> pageView = previewService.briefSearchForFile(briefSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("fileList");
		return mav;
	}
	
	@RequestMapping("/briefSearchweixin")
	public ModelAndView briefSearchweixin(@Valid BriefSearchPara briefSearch){
		ModelAndView mav = new ModelAndView();
		PageView<TaskPrePara> pageView = previewService.briefSearchForTask(briefSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("weixinList");
		return mav;
	}
	
	@RequestMapping("/advSearchweixin")
	public ModelAndView advSearchweixin(@Valid AdvSearchPara advSearch){
		ModelAndView mav = new ModelAndView();
		PageView<TaskPrePara> pageView = previewService.advSearchForTask(advSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("weixinList");
		return mav;
	}
	
	@RequestMapping("/briefSearchpage")
	public ModelAndView briefSearchpage(@Valid BriefSearchPara briefSearch){
		ModelAndView mav = new ModelAndView();
		PageView<PagePrePara> pageView = previewService.briefSearchForPage(briefSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("pageList");
		return mav;
	}
	
	@RequestMapping("/advSearchpage")
	public ModelAndView advSearchpage(@Valid AdvSearchPara advSearch){
		ModelAndView mav = new ModelAndView();
		PageView<PagePrePara> pageView = previewService.advSearchForPage(advSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("pageList");
		return mav;
	}
	
	@RequestMapping("/briefSearchfile")
	public ModelAndView briefSearchfile(@Valid BriefSearchPara briefSearch){
		ModelAndView mav = new ModelAndView();
		PageView<FilePrePara> pageView = previewService.briefSearchForFile(briefSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("fileList");
		return mav;
	}
	
	@RequestMapping("/advSearchfile")
	public ModelAndView advSearchfile(@Valid AdvSearchPara advSearch){
		ModelAndView mav = new ModelAndView();
		PageView<FilePrePara> pageView = previewService.advSearchForFile(advSearch);
		mav.addObject("pageView", pageView);
		mav.setViewName("fileList");
		return mav;
	}
	
	@RequestMapping("getFileListByPageId")
	public ModelAndView getFileListByPageId(String pageId, String curPage){
		ModelAndView mav = new ModelAndView();
		PageView<FilePrePara> pageView = previewService.getFileListByPageId(pageId, curPage);
		mav.addObject("pageView", pageView);
		mav.addObject("pageId", pageId);
		mav.setViewName("fileList");
		return mav;
	}
	
	@RequestMapping("getPageListByTaskId")
	public ModelAndView getPageListByTaskId(String taskId, String curPage){
		ModelAndView mav = new ModelAndView();
		PageView<PagePrePara> pageView = previewService.getPageListByTaskId(taskId, curPage);
		mav.addObject("pageView", pageView);
		mav.addObject("taskId", taskId);
		mav.setViewName("pageList");
		return mav;
	}
	
	@RequestMapping("genReport")
	public void genReport(HttpServletResponse response, HttpServletRequest request){
		
		String siteId = request.getParameter("siteId");
		String siteName = request.getParameter("siteName");
		try {
			//表单提交方式为GET时，提交内容包含中文时，需采用一下方式进行转码，否则中文为乱码
			byte[] source = siteName.getBytes("iso8859-1");
			siteName = new String(source, "UTF-8");
			String filename = siteName + "检查结果报告.doc";
			response.setHeader("Content-Type","application/msword");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			previewService.getReportData(response.getOutputStream(), siteId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 批量导出一至多个网站检查结果，现在还没调试正确，ftl文件表达式的问题
	 * @param response
	 * @param request
	 */
	@RequestMapping("sendObjectArr")
	public void sendObjectArr(HttpServletResponse response, HttpServletRequest request){
		
		String str = request.getParameter("siteInfo");
		try {
			//表单提交方式为GET时，提交内容包含中文时，需采用一下方式进行转码，否则中文为乱码
			byte[] source = str.getBytes("iso8859-1");
			str = new String(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String filename = "";
		String siteIdList = "";
		JSONArray arr = JSONArray.fromObject(str);
		int length = arr.size();
		if(length == 1){
			JSONObject obj = (JSONObject) arr.get(0);
			String siteId = obj.getString("id");
			filename = obj.getString("name") + "检查结果报告.doc";
			siteIdList += siteId;
		}
		else{
			filename = "多个网站检查结果报告.doc";
			for(int i=0; i<length; i++){
				JSONObject obj = (JSONObject) arr.get(i);
				String siteId = obj.getString("id");
				siteIdList += "'" + siteId + "',";
			}
			siteIdList = siteIdList.substring(0, siteIdList.length() - 1); 
		}
		response.setHeader("Content-Type","application/msword");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			previewService.getReportData(response.getOutputStream(), siteIdList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);
	}
	
	//测试下载服务器中的本地文件
	@RequestMapping("generateReport")
	public void generateReport(HttpServletResponse response){
		response.setHeader("Content-Type","application/msword");
		response.setHeader("Content-Disposition", "attachment;filename=outfile.doc");
		OutputStream os = null;
		FileInputStream fis = null;
		try {
			os = response.getOutputStream();
			
			fis = new FileInputStream("D:/javaDoc/outFile.doc");
			byte[] tmp = new byte[1024];

			int readLen = -1;

			while ((readLen = fis.read(tmp)) != -1) {

				os.write(tmp, 0, readLen);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//测试下载服务器生成的word文档
	@RequestMapping("genReportTest")
	public void getReportTest(HttpServletResponse response){
		TestExport tr = new TestExport();
		response.setHeader("Content-Type","application/msword");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("测试文件.doc".getBytes("gb2312"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tr.genReport(response);
	}
	
	@RequestMapping("dealContext")
	public ModelAndView dealContext(String pageId, String file_url, String file_name){
		ModelAndView mav = new ModelAndView();
		
		String file_name1 ="";
		try {
			byte[] source = file_name.getBytes("iso8859-1");
			file_name1 = new String(source, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<KeyContext> keyContextList = previewService.keyContextList(pageId);
		
		mav.addObject("file_id", pageId);
		mav.addObject("file_url", file_url);
		mav.addObject("file_name", file_name1);
		mav.addObject("keyContextList", keyContextList);
		mav.setViewName("dealContext");
		return mav;
	}
	
	@RequestMapping("saveContextResult")
	public void saveContextResult(String file_id, String contextResultList, HttpServletResponse response){
		String result = previewService.saveContextResult(file_id, contextResultList);
		
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping("getPageSecureResult")
	public void getPageSecureResult(String pageId, HttpServletResponse response){
		String result = previewService.getPageSecureResult(pageId);
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
