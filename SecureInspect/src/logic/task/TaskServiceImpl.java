package logic.task;

import java.util.LinkedList;
import java.util.List;

import mvc.account.TaskPara;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import utils.ConverStringUtil;
import utils.PageView;
import utils.PathUtil;
import db.table.keyTask.bean.KEYTASK;
import db.table.keyTask.dao.KeyTaskDao;
import db.table.keyword.bean.KEYWORD;
import db.table.keyword.dao.KeywordDAO;
import db.table.task.bean.TASK;
import db.table.task.dao.TaskDAO;


@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	@Qualifier("taskDao")
	private TaskDAO taskDao;
	
	@Autowired
	@Qualifier("keywordDao")
	private KeywordDAO keywordDao;
	
	@Autowired
	@Qualifier("keyTaskDao")
	private KeyTaskDao keyTaskDao;
	
	public void getKeywordList(List<KEYWORD> keywordList) {
		String sql1 = "select * from keyword";
		@SuppressWarnings("unchecked")
		List<KEYWORD> keys = (List<KEYWORD>) keywordDao.findByQuery(sql1, KEYWORD.class);
		for(KEYWORD key : keys){
			KEYWORD tp = key;
			keywordList.add(tp);
		}
	}
	
	/**
	 * 为任务管理页面提供数据
	 */
	@Override
	public void findTaskInfo(PageView<TaskPara> pageview, int curPage, String type) {
		
		
		String sql2 = "select * from task where type = '" + type +"'";
		int totalNum = (int) taskDao.getTotalItemsNumBySelectQuery(sql2);
		if(totalNum < 1) return;
		PageView<TASK> temp = new PageView<TASK>(10);
		pageview.setTotalNum(totalNum);
		temp.setTotalNum(totalNum);
		pageview.setCurPage(curPage);
		
		temp.setCurPage(curPage);
		taskDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql2, TASK.class, temp);
		
		List<TaskPara> taskList = new LinkedList<TaskPara>();
		for(TASK task : temp.getItems()){
			TaskPara taskpara = new TaskPara();
			taskpara.setKwds(keywordDao.getKwdsByTaskId(task.getId()));
			taskpara.setTaskName(task.getName());
			
			taskpara.setLocation(task.getLocation());
			
			taskpara.setTime(ConverStringUtil.convertLongTime(task.getCreate_time()));
			taskpara.setStatus(task.getStatus());
			taskpara.setTaskId(task.getId());
			taskList.add(taskpara);
		}
		pageview.setItems(taskList);
	}
	
	@Override
	public String saveScanTask(String type, String kwds, String url, String name) {
		// TODO Auto-generated method stub
		
		TASK task = new TASK();
		task.setCreate_time(System.currentTimeMillis());
		
		
		task.setLocation(url);
		task.setName(name);
		task.setStatus("未开始");
		task.setType(type);
		taskDao.save(task);
		String taskId = task.getId();
		String[] kwdL = kwds.split(",");
		kwds = "";
		for(String kwd : kwdL){
			kwds += "'" + kwd + "',";
		}
		kwds = (String) kwds.subSequence(0, kwds.length()-1);
		String sql1 = "select id from keyword where value in (" + kwds + ")";
		@SuppressWarnings("unchecked")
		List<String> keyIdList = (List<String>) keywordDao.findByQueryForList(sql1, String.class);
		List<KEYTASK> keyList = new LinkedList<KEYTASK>();
		for(String keyId : keyIdList){
			KEYTASK keytask = new KEYTASK();
			keytask.setTask_id(taskId);
			keytask.setKeyword_id(keyId);
			keyList.add(keytask);
		}
		keyTaskDao.batchSave(keyList);
		return task.getId();
	}

	@Override
	public int updateTaskStatus(String task_id) {
		// TODO Auto-generated method stub
		
		taskDao.updateStatus("检查中", task_id);
		return 0;
	}
}
