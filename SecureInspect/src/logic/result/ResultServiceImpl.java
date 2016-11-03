package logic.result;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.preview.PageUrlCnt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import db.table.context.dao.ContextDAO;
import db.table.contextResult.bean.CONTEXTRESULT;
import db.table.contextResult.dao.ContextResultDAO;
import db.table.fileStatistics.bean.FILESTATISTICS;
import db.table.fileStatistics.dao.FileStatisticsDAO;
import db.table.keyword.dao.KeywordDAO;
import db.table.resourceFile.bean.RESOURCEFILE;
import db.table.resourceFile.dao.ResourceFileDAO;
import db.table.taskStatistics.bean.TASKSTATISTICS;
import db.table.taskStatistics.dao.TaskStatisticsDAO;
import db.table.task.dao.TaskDAO;

@Service("resultService")
public class ResultServiceImpl  implements ResultService{
	
	@Autowired
	@Qualifier("resourceFileDao")
	private ResourceFileDAO resourceFileDao;
	
	@Autowired
	@Qualifier("taskStatisticsDao")
	private TaskStatisticsDAO taskStatisticsDao;
	
	@Autowired
	@Qualifier("fileStatisticsDao")
	private FileStatisticsDAO fileStatisticsDao;
	
	@Autowired
	@Qualifier("taskDao")
	private TaskDAO taskDao;
	
	@Autowired
	@Qualifier("keywordDao")
	private KeywordDAO keywordDao;
	
	@Autowired
	@Qualifier("contextDao")
	private ContextDAO contextDao;
	
	@Autowired
	@Qualifier("contextResultDao")
	private ContextResultDAO contextResultDao;
	
	private String fileType = "page";
	
	private String filterHtmlTag(String content){
		String regxpForHtml = "<([^>\u2E80-\u9FFF]*)>|<|>";
		Pattern pattern = Pattern.compile(regxpForHtml);   
        Matcher matcher = pattern.matcher(content);   
        StringBuffer sb = new StringBuffer();   
        boolean result1 = matcher.find();   
        while (result1) {   
            matcher.appendReplacement(sb, "");   
            result1 = matcher.find();   
        }   
        matcher.appendTail(sb);
        return sb.toString();  
	}

	private String filterBadHtmlTag(String content){
		String regxpForHtml = "<([^>\u2E80-\u9FFF]*)>|<|>|&nbsp;|&nbs";
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		boolean result = matcher.find();
		while(result){
			matcher.appendReplacement(sb, "");
			result = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	//需要修改，taskStatistics的type需要与file的type有关系
	private void saveTaskInfo(String taskId, int total, String validFlag){
		if(validFlag.equals("invalid")){
			taskDao.updateStatus("地址无效", taskId);
		}
		else{
			taskDao.updateStatus("检查结束", taskId);
		}
		
		TASKSTATISTICS taskStatistics = new TASKSTATISTICS();
		taskStatistics.setTask_id(taskId);
		taskStatistics.setType(fileType);
		taskStatistics.setNo_deal_num(Integer.valueOf(total).toString());
		taskStatistics.setTotal_num(Integer.valueOf(total).toString());
		taskStatistics.setCreate_time(System.currentTimeMillis());
		taskStatisticsDao.save(taskStatistics);
		
		String sql4 = "update task set total = " + total + " where id='" + taskId + "';";
		taskDao.executeBySql(sql4);
	}
	
	@Override
	public void saveResult(String validFlag, String taskid){
		if(validFlag.equals("invalid")){
			long t = System.currentTimeMillis();
			String sqlt = "update task set status='地址无效', total =0,secret_num=0,end_time=" + t + " where id='" + taskid + "';";
			taskDao.executeBySql(sqlt);
			return;
		}
		
		//初始化context表对应的contextResult
		String sqlContext = "select id from context where resourcefile_id in (select id from resourcefile " +
				          "where task_id = '" + taskid + "')";
		List<String> contextIdList = (List<String>) contextDao.findByQueryForList(sqlContext, String.class);
		List<CONTEXTRESULT> contextResultList = new LinkedList<CONTEXTRESULT>();
		for(String contextId : contextIdList){
			CONTEXTRESULT contextResult = new CONTEXTRESULT();
			contextResult.setContext_id(contextId);
			contextResult.setCreate_time(System.currentTimeMillis());
			contextResult.setSecret_result("wait");
			contextResultList.add(contextResult);
		}
		contextResultDao.batchSave(contextResultList);
		Map<String, Integer> tn = new HashMap<String, Integer>();
		//查找网站正文类型的文件
		String sqlfs1 = "select id from resourcefile where task_id ='" + taskid + "' and file_url = page_url";
		
		//网页Idlist，特点：file_url == page_url
		List<String> pageIdList = (List<String>) resourceFileDao.findByQueryForList(sqlfs1, String.class);
		List<FILESTATISTICS> pageStatisticsList = new LinkedList<FILESTATISTICS>();
		for(String fileId : pageIdList){
			String sqlc = "select count(*) from context where resourcefile_id = '" + fileId + "'";
			List<Integer> cnt = (List<Integer>) contextDao.findByQueryForList(sqlc, Integer.class);
			FILESTATISTICS fileStatistics = new FILESTATISTICS();
			fileStatistics.setCreate_time(System.currentTimeMillis());
			fileStatistics.setResourcefile_id(fileId);
			fileStatistics.setTotal_num(cnt.get(0));
			fileStatistics.setSecret_result("wait");
			fileStatistics.setContain_file_flag("no");
			pageStatisticsList.add(fileStatistics);
		}
		
		fileStatisticsDao.batchSave(pageStatisticsList);
		tn.put("page", pageIdList.size());
		
		String sqlfs2 = "select id, page_url from resourcefile where task_id ='" + taskid + "' and file_url != page_url"; 
		
		//文件list，特点：file_url != page_url
		List<RESOURCEFILE> fileList = (List<RESOURCEFILE>) resourceFileDao.findByQuery(sqlfs2, RESOURCEFILE.class);
		
		//文件统计List
		List<FILESTATISTICS> fileStatisticsList = new LinkedList<FILESTATISTICS>();
		
		for(RESOURCEFILE file : fileList){
			String sqlc = "select count(*) from context where resourcefile_id = '" + file.getId() + "'";
			List<Integer> cnt = (List<Integer>) contextDao.findByQueryForList(sqlc, Integer.class);
			FILESTATISTICS fileStatistics = new FILESTATISTICS();
			fileStatistics.setCreate_time(System.currentTimeMillis());
			fileStatistics.setResourcefile_id(file.getId());
			fileStatistics.setTotal_num(cnt.get(0));
			fileStatistics.setSecret_result("wait");
			fileStatistics.setContain_file_flag("no");
			fileStatisticsList.add(fileStatistics);
		}
		
		//查找包含该文件的网页，若有，则更新对应的包含文件标志；
		String sqlf = "update filestatistics set contain_file_flag = 'true', total_num = total_num+1 where"
				+ " resourcefile_id in (select id from resourcefile where file_url in "
				+ "(select page_url from resourcefile where file_url != page_url and task_id='" + taskid + "')"
				+ " and task_id='" + taskid + "')";
		fileStatisticsDao.executeBySql(sqlf);
		
		//若文件的父级网页不存在，新建的父级网页对象List，用来插入新纪录
		List<RESOURCEFILE> pFileList = new LinkedList<RESOURCEFILE>();
		String sqlfn = "select count(page_url) as cnt, page_url as para from resourcefile where file_url != page_url and"
				+ " page_url not in (select page_url from resourcefile where file_url = page_url) and task_id = '" +taskid + "'"
				+ "group by page_url";
		List<PageUrlCnt> pageUrlCntList = (List<PageUrlCnt>) resourceFileDao.findByQuery(sqlfn, PageUrlCnt.class);
		for(PageUrlCnt pageUrlCnt : pageUrlCntList){
			RESOURCEFILE rfile = new RESOURCEFILE();
			rfile.setCreate_time(System.currentTimeMillis());
			rfile.setPage_url(pageUrlCnt.getPara());
			rfile.setTask_id(taskid);
			rfile.setType("page");
			
			FILESTATISTICS filests = new FILESTATISTICS();
			filests.setContain_file_flag("yes");
			filests.setCreate_time(System.currentTimeMillis());
			filests.setResourcefile_id(rfile.getId());
			filests.setTotal_num(pageUrlCnt.getCnt());
			filests.setSecret_result("wait");
			LinkedList<FILESTATISTICS> tempfilesList = new LinkedList<FILESTATISTICS>();
			tempfilesList.add(filests);
			rfile.setFileStatisticsList(tempfilesList);
			pFileList.add(rfile);
		}
		
		fileStatisticsDao.batchSave(fileStatisticsList);
		resourceFileDao.cascadedBatchSave(pFileList);
		
		int fileNum = pageIdList.size() + pFileList.size();
		this.saveTaskInfo(taskid, fileNum, "valid");
	}
}
