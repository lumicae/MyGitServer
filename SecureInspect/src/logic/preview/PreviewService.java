package logic.preview;

import java.io.OutputStream;
import java.util.List;

import mvc.preview.AdvSearchPara;
import mvc.preview.BriefSearchPara;
import mvc.preview.FilePrePara;
import mvc.preview.KeyContext;
import mvc.preview.PagePrePara;
import mvc.preview.TaskPrePara;
import net.sf.json.JSONArray;
import utils.PageView;

public interface PreviewService {

	public PageView<TaskPrePara> briefSearchForTask(BriefSearchPara briefSearchPara);
	
	public PageView<TaskPrePara> advSearchForTask(AdvSearchPara advSearchPara);
	
	public void getReportData(OutputStream os, String ids);

	public JSONArray getTaskKeyword(String taskId);
	
	public PageView<PagePrePara> briefSearchForPage(BriefSearchPara briefSearchPara);
	
	public PageView<FilePrePara> briefSearchForFile(BriefSearchPara briefSearchPara);
	
	public PageView<PagePrePara> advSearchForPage(AdvSearchPara advSearchPara);
	
	public PageView<FilePrePara> advSearchForFile(AdvSearchPara advSearchPara);
	
	public PageView<PagePrePara> getPageListByTaskId(String taskId, String curPage);
	
	public PageView<FilePrePara> getFileListByPageId(String pageId, String curPage);
	
	public List<KeyContext> keyContextList(String fileId);
	
	public String saveContextResult(String fileId, String contextResultList);
	
	public String getPageSecureResult(String pageId);
	
	public String getRSrcByPageId(String pageId);
}
