package logic.task;

import java.util.List;

import db.table.keyword.bean.KEYWORD;
import mvc.account.TaskPara;
import utils.PageView;


public interface TaskService {

	public String saveScanTask(String type, String kwds, String url, String name);
	
	public int updateTaskStatus(String task_id);
	
	public void findTaskInfo(PageView<TaskPara> pageview, int curPage, String type);
	
	public void getKeywordList(List<KEYWORD> keywordList);
	
}
