package logic.preview;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mvc.preview.AdvSearchPara;
import mvc.preview.BriefSearchPara;
import mvc.preview.FilePrePara;
import mvc.preview.KeyContext;
import mvc.preview.PagePrePara;
import mvc.preview.TaskPrePara;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import utils.ConverStringUtil;
import utils.LoadWordTemplate;
import utils.PageView;
import db.table.context.bean.CONTEXT;
import db.table.context.dao.ContextDAO;
import db.table.contextResult.dao.ContextResultDAO;
import db.table.fileStatistics.bean.FILESTATISTICS;
import db.table.fileStatistics.dao.FileStatisticsDAO;
import db.table.keyword.bean.KEYWORD;
import db.table.keyword.dao.KeywordDAO;
import db.table.resourceFile.bean.RESOURCEFILE;
import db.table.resourceFile.dao.ResourceFileDAO;
import db.table.task.bean.TASK;
import db.table.task.dao.TaskDAO;
import db.table.taskStatistics.bean.TASKSTATISTICS;
import db.table.taskStatistics.dao.TaskStatisticsDAO;

@Service("previewService")
public class PreviewServiceImpl implements PreviewService {

	@Autowired
	@Qualifier("taskDao")
	private TaskDAO taskDAO;

	@Autowired
	@Qualifier("taskStatisticsDao")
	private TaskStatisticsDAO taskStatisticsDao;
	
	@Autowired
	@Qualifier("resourceFileDao")
	private ResourceFileDAO resourceFileDao;
	
	@Autowired                     
	@Qualifier("fileStatisticsDao")
	private FileStatisticsDAO fileStatisticsDao;
	
	@Autowired
	@Qualifier("contextDao")
	private ContextDAO contextDao;
	
	@Autowired
	@Qualifier("keywordDao")
	private KeywordDAO keywordDao;
	
	@Autowired
	@Qualifier("contextResultDao")
	private ContextResultDAO contextResultDao;
	
	//secType 是涉密类型
	private String SECTYPE = "secret";
	
	//susType 是可疑类型
	private String SUSTYPE = "suspect";
	
	//图片文件
	private String PICTUREFILE = "picture";
	
	//网页文件
	private String PAGEFILE = "page";
	
	//附件文件
	private String ATTACHFILE = "attachment";
	
	@Override
	public PageView<TaskPrePara> briefSearchForTask(BriefSearchPara briefSearch) {
		// TODO Auto-generated method stub
		String taskType = briefSearch.getTaskType();
		String type = briefSearch.getBriefSrchKey();
		String val = briefSearch.getBriefSrchVal();
		String sql = "";
		if(val == null || "".equals(val)){
			sql = "select * from task where type = '" + taskType + "'";
		}
		else if("关键字".equals(type)){
			sql = "select task.* from task join keytask, keyword where keytask.task_id=task.id and keyword.id=keytask.keyword_id and keyword.value='"+ val + "' and task.type ='" + taskType + "'";
		}
		else if("task_name".equals(type)){
			sql = "select * from task where name ='" + val + "' and type ='" + taskType + "'";
		}
		int totalNum = (int) taskDAO.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int pagesize = Integer.valueOf(briefSearch.getPageSize());
		int curpage = 1;
		if(!briefSearch.getCurPage().equals("")){
			curpage = Integer.valueOf(briefSearch.getCurPage());
		}
		PageView<TASK> temp= new PageView<TASK>(pagesize);
		PageView<TaskPrePara> pageView = new PageView<TaskPrePara>(pagesize);
		temp.setTotalNum(totalNum);
		temp.setCurPage(curpage);
		pageView.setTotalNum(totalNum);
		pageView.setCurPage(curpage);
		taskDAO.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, TASK.class, temp);
		
		List<TaskPrePara> items = new LinkedList<TaskPrePara>();
		if(temp.getItems().size() < 1)
			return pageView;
		for(TASK task : temp.getItems()){
			TaskPrePara item = new TaskPrePara();
			item.setTaskId(task.getId());
			item.setEndTime(new Date(task.getEnd_time()));
			item.setName(task.getName());
			item.setSecureCnt(task.getSecret_num());
			item.setStartTime(new Date(task.getCreate_time()));
			item.setStatus(task.getStatus());
			item.setTotal(task.getTotal());
			item.setLocation(task.getLocation());
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
	}

	@Override
	public PageView<TaskPrePara> advSearchForTask(AdvSearchPara advSearch) {
		String taskType = advSearch.getTaskType();
		String sql = "select * from task where type ='" + taskType + "' and ";
		String type = advSearch.getBriefSrchKey();
		String val = advSearch.getBriefSrchVal();
		Date startDate = ConverStringUtil.parseDate(null, advSearch.getStartTime());
		Date endDate = ConverStringUtil.parseDate(null, advSearch.getEndTime());
		long startTime = (startDate==null?0:startDate.getTime());
		long endTime = (endDate==null?0:endDate.getTime());
		
		String fileName = advSearch.getFileName();
		String secCondition = advSearch.getSecCondition();
		String location = advSearch.getLocation();
		String column = "";
		switch(secCondition){
			case "wait" : column = "no_deal_num>0";break;
			default : column = secCondition + "_num>0";break;
		}
		sql += " id in (select task_id from taskStatistics where " + column +")";
		if(fileName != ""){
			sql += " and id in (select task_id from resourcefile where name ='" + fileName + "')";
		}
		
		if(val != ""){
			switch(type){
			case "task_name" : sql += " and name ='" + val + "'";
				break;
			case "关键字" : sql += " and id in (select task_id from keytask where keyword_id in (select id from keyword where value='" + val + "'))";
				break;
			case "" :
				break;
			default :
				sql += "";
			}
		}
		
		if(location != ""){
			sql += " and location='" + location + "'";
		}
		
		if(startTime > 0 && endTime >0){
			long t = 86400000l -1; //24 * 60 * 60 * 1000;
			endTime +=t;
			sql += " and create_time between " + startTime + " and " + endTime;
		}
		else if(startTime >0){
			sql += " and create_time > " + startTime;
		}
		else if(endTime >0){
			long t = 86400000l -1; //24 * 60 * 60 * 1000;
			endTime +=t;
			sql += " and create_time < " + endTime;
		}
		int totalNum = (int) taskDAO.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int curpage = 1;
		if(!advSearch.getCurPage().equals("")){
			curpage = Integer.valueOf(advSearch.getCurPage());
		}
		int pageSize = Integer.valueOf(advSearch.getPageSize());
		PageView<TASK> temp= new PageView<TASK>(pageSize);
		temp.setTotalNum(totalNum);
		temp.setCurPage(curpage);
		PageView<TaskPrePara> pageView = new PageView<TaskPrePara>(pageSize);
		pageView.setTotalNum(totalNum);
		pageView.setCurPage(curpage);
		pageView.setPageSize(pageSize);
		taskDAO.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, TASK.class, temp);
		
		List<TaskPrePara> items = new LinkedList<TaskPrePara>();
		for(TASK task : temp.getItems()){
			TaskPrePara item = new TaskPrePara();
			item.setEndTime(new Date(task.getEnd_time()));
			item.setName(task.getName());
			item.setSecureCnt(task.getSecret_num());
			item.setStartTime(new Date(task.getCreate_time()));
			item.setStatus(task.getStatus());
			item.setTotal(task.getTotal());
			item.setLocation(task.getLocation());
			item.setTaskId(task.getId());
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageView<PagePrePara> briefSearchForPage(BriefSearchPara briefSearchPara) {
		// TODO Auto-generated method stub
		String type = briefSearchPara.getBriefSrchKey();
		String val = briefSearchPara.getBriefSrchVal();
		String sql = "";
		if("task_name".equals(type)){
			sql = "select * from resourceFile where (page_url=file_url or file_url is null) and task_id in (select id from task where name='" + val + "')";
		}
		else if("关键字".equals(type)){
			sql = "select * from resourceFile where (page_url=file_url or file_url is null) and task_id in (select task_id from  keytask where keyword_id in (select id from keyword where value='"+ val + "'))";
		}
		else{
			sql = "select * from resourceFile where (page_url=file_url or file_url is null)";
		}
		sql += " and task_id in (select id from task where type='" + briefSearchPara.getTaskType() + "')";
		//sql += " and id in ( select file_id from filestatistics where totalNum >0)";
		int curPage = 1;
		if(!briefSearchPara.getCurPage().equals("")){
			curPage = Integer.valueOf(briefSearchPara.getCurPage());
		}
		int totalNum = (int) resourceFileDao.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int pageSize = Integer.valueOf(briefSearchPara.getPageSize());
		PageView<RESOURCEFILE> temp= new PageView<RESOURCEFILE>(pageSize);
		PageView<PagePrePara> pageView = new PageView<PagePrePara>(pageSize);
		pageView.setTotalNum(totalNum);
		temp.setTotalNum(totalNum);
		pageView.setCurPage(curPage);
		temp.setCurPage(curPage);
		resourceFileDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, RESOURCEFILE.class, temp);
		List<PagePrePara> items = new LinkedList<PagePrePara>();
		if(temp.getItems().size() < 1)
			return pageView;
		for(RESOURCEFILE rfile : temp.getItems()){
			PagePrePara item = new PagePrePara();
			item.setTitle(rfile.getTitle());
			item.setSrc(rfile.getRelative_path()==null ? "正文未检出" : rfile.getRelative_path());
			item.setURL(rfile.getPage_url());
			item.setPageId(rfile.getId());
			List<KeyContext> keyContextList = new LinkedList<KeyContext>();
			String fileId = rfile.getId();
			String sqlc = "select * from context where resourcefile_id='" + fileId+ "'";
			List<CONTEXT> contextList = (List<CONTEXT>) contextDao.findByQuery(sqlc, CONTEXT.class);
			for(CONTEXT context : contextList){
				KeyContext keyContext = new KeyContext();
				keyContext.setContext(filterBadHtmlTag(context.getText()));
				keyContext.setContext_id(context.getId());
				keyContext.setKey(context.getKeyword());
				
				keyContextList.add(keyContext);
			}
			FILESTATISTICS fileStatistics = fileStatisticsDao.findByFile_id(fileId);
			
			item.setIresult(fileStatistics.getSecret_result());
			item.setKeyContextList(keyContextList);
			item.setSecureCnt(fileStatistics.getSecret_num());
			item.setContainFileFlag(fileStatistics.getContain_file_flag());
			
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
	}

	@Override
	public PageView<FilePrePara> briefSearchForFile(BriefSearchPara briefSearchPara) {
		// TODO Auto-generated method stub
		String type = briefSearchPara.getBriefSrchKey();
		String val = briefSearchPara.getBriefSrchVal();
		String sql = "";
		if("task_name".equals(type)){
			sql = "select * from resourceFile where page_url != file_url and task_id in (select id from task where name='" + val + "')";
		}
		else if("关键字".equals(type)){
			sql = "select * from resourceFile where page_url != file_url and task_id in (select task_id from keytask where keyword_id in (select id from keyword where value='"+ val + "'))";
		}
		else{
			sql = "select * from resourceFile where page_url != file_url";
		}
		sql += " and id in ( select resourcefile_id from filestatistics where total_num >0)";
		sql += " and task_id in (select id from task where type='" + briefSearchPara.getTaskType() + "')";
		int curPage = 1;
		if(!briefSearchPara.getCurPage().equals("")){
			curPage = Integer.valueOf(briefSearchPara.getCurPage());
		}
		int totalNum = (int) resourceFileDao.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int pageSize = Integer.valueOf(briefSearchPara.getPageSize());
		PageView<RESOURCEFILE> temp= new PageView<RESOURCEFILE>(pageSize);
		PageView<FilePrePara> pageView = new PageView<FilePrePara>(pageSize);
		pageView.setTotalNum(totalNum);
		temp.setTotalNum(totalNum);
		pageView.setCurPage(curPage);
		temp.setCurPage(curPage);
		resourceFileDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, RESOURCEFILE.class, temp);
		
		List<FilePrePara> items = new LinkedList<FilePrePara>();
		if(temp.getItems().size() < 1)
			return pageView;
		for(RESOURCEFILE rfile : temp.getItems()){
			FilePrePara item = new FilePrePara();
			item.setTitle(rfile.getTitle());
			item.setSrc(rfile.getRelative_path());
			item.setURL(rfile.getFile_url());
			item.setFileId(rfile.getId());
			List<KeyContext> keyContextList = new LinkedList<KeyContext>();
			String fileId = rfile.getId();
			String sqlc = "select * from context where resourcefile_id='" + fileId+ "'";
			@SuppressWarnings("unchecked")
			List<CONTEXT> contextList = (List<CONTEXT>) contextDao.findByQuery(sqlc, CONTEXT.class);
			for(CONTEXT context : contextList){
				KeyContext keyContext = new KeyContext();
				
				keyContext.setContext(filterBadHtmlTag(context.getText()));
				keyContext.setContext_id(context.getId());
				keyContext.setKey(context.getKeyword());

				keyContextList.add(keyContext);
			}
			FILESTATISTICS fileStatistics = fileStatisticsDao.findByFile_id(fileId);
			
			item.setIresult(fileStatistics.getSecret_result());
			item.setKeyContextList(keyContextList);
			item.setSecureCnt(fileStatistics.getSecret_num());
			item.setContainFileFlag(fileStatistics.getContain_file_flag());
			String fileName = rfile.getRelative_path();
			String prefix= fileName.substring(fileName.lastIndexOf(".")+1);
			item.setType(prefix);
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
	}

	@Override
	public PageView<PagePrePara> advSearchForPage(AdvSearchPara advSearchPara) {
		// TODO Auto-generated method stub
		
		String type = advSearchPara.getBriefSrchKey();
		String val = advSearchPara.getBriefSrchVal();
		Date startDate = ConverStringUtil.parseDate(null, advSearchPara.getStartTime());
		Date endDate = ConverStringUtil.parseDate(null, advSearchPara.getEndTime());
		long startTime = (startDate==null?0:startDate.getTime());
		long endTime = (endDate==null?0:endDate.getTime());
		
		String fileName = advSearchPara.getFileName();
		String secCondition = advSearchPara.getSecCondition();
		String location = advSearchPara.getLocation();
		
		String sql = "select * from resourcefile where (page_url=file_url or file_url is null) and id in "
				+ "( select resourcefile_id from fileStatistics where secret_result='" + secCondition + "')";
		if(fileName != ""){
			sql += " and name ='" + fileName + "'";
		}
		if(location != ""){
			sql += " and task_id in (select id from task where location='" + location + "')";
		}
		
		if(startTime >0 && endTime>0){
			long t = 86400000l -1; //24 * 60 * 60 * 1000;
			endTime +=t;
			sql += " and task_id in (select id from task where create_time between " + startTime + " and " + endTime + " )";
		}
		else if(startTime >0){
			sql += " and task_id in (select id from task where create_time > " + startTime + " )";
		}
		else if(endTime>0){
			long t = 86400000l -1; //24 * 60 * 60 * 1000;
			endTime +=t;
			sql += " and task_id in (select id from task where create_time < " + endTime + " )";
		}
		if(val != ""){
			if(type.equals("task_name")){
				sql += " and task_id in (select id from task where name='" + val + "')";
			}
			if(type.equals("关键字")){
				sql += " and task_id in (select task_id from keytask where keyword_id in (select id from keyword "
						+ "where value='" + val + "'))";
			}
		}
		sql += " and task_id in (select id from task where type='" + advSearchPara.getTaskType() + "')";
		int curPage =1;
		if(!advSearchPara.getCurPage().equals("")){
			curPage = Integer.valueOf(advSearchPara.getCurPage());
		}
		int totalNum = (int) resourceFileDao.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int pageSize = Integer.valueOf(advSearchPara.getPageSize());
		PageView<RESOURCEFILE> temp= new PageView<RESOURCEFILE>(pageSize);
		PageView<PagePrePara> pageView = new PageView<PagePrePara>(pageSize);
		pageView.setTotalNum(totalNum);
		temp.setTotalNum(totalNum);
		pageView.setCurPage(curPage);
		temp.setCurPage(curPage);
		resourceFileDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, RESOURCEFILE.class, temp);
		
		List<PagePrePara> items = new LinkedList<PagePrePara>();
		if(temp.getItems().size() < 1)
			return pageView;
		for(RESOURCEFILE rfile : temp.getItems()){
			PagePrePara item = new PagePrePara();
			item.setTitle(rfile.getTitle());
			item.setSrc(rfile.getRelative_path()==null ? "正文未检出" : rfile.getRelative_path());
			item.setURL(rfile.getPage_url());
			item.setPageId(rfile.getId());
			List<KeyContext> keyContextList = new LinkedList<KeyContext>();
			String fileId = rfile.getId();
			String sqlc = "select * from context where resourcefile_id='" + fileId+ "'";
			@SuppressWarnings("unchecked")
			List<CONTEXT> contextList = (List<CONTEXT>) contextDao.findByQuery(sqlc, CONTEXT.class);
			for(CONTEXT context : contextList){
				KeyContext keyContext = new KeyContext();
				keyContext.setContext(filterBadHtmlTag(context.getText()));
				keyContext.setContext_id(context.getId());
				keyContext.setKey(context.getKeyword());

				keyContextList.add(keyContext);
			}
			FILESTATISTICS fileStatistics = fileStatisticsDao.findByFile_id(fileId);
			
			item.setIresult(fileStatistics.getSecret_result());
			item.setKeyContextList(keyContextList);
			item.setSecureCnt(fileStatistics.getSecret_num());
			item.setContainFileFlag(fileStatistics.getContain_file_flag());
			
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
	}

	@Override
	public PageView<FilePrePara> advSearchForFile(AdvSearchPara advSearchPara) {
		// TODO Auto-generated method stub
		
		String type = advSearchPara.getBriefSrchKey();
		String val = advSearchPara.getBriefSrchVal();
		Date startDate = ConverStringUtil.parseDate(null, advSearchPara.getStartTime());
		Date endDate = ConverStringUtil.parseDate(null, advSearchPara.getEndTime());
		long startTime = (startDate==null?0:startDate.getTime());
		long endTime = (endDate==null?0:endDate.getTime());
		
		String fileName = advSearchPara.getFileName();
		String secCondition = advSearchPara.getSecCondition();
		String location = advSearchPara.getLocation();
		
		String sql = "select * from resourcefile where page_url != file_url and id in "
				+ "( select resourcefile_id from fileStatistics where secret_result='" + secCondition + "')";
		if(fileName != ""){
			sql += " and name ='" + fileName + "'";
		}
		if(location != ""){
			sql += " and task_id in (select id from task where location='" + location + "')";
		}
		
		if(startTime >0 && endTime>0){
			long t = 86400000l -1; //24 * 60 * 60 * 1000;
			endTime +=t;
			sql += " and task_id in (select id from task where create_time between " + startTime + " and " + endTime + " )";
		}
		else if(startTime >0){
			sql += " and task_id in (select id from task where create_time > " + startTime + " )";
		}
		else if(endTime >0){
			long t = 86400000l -1; //24 * 60 * 60 * 1000;
			endTime +=t;
			sql += " and task_id in (select id from task where create_time < " + endTime + " )";
		}
		if(val != ""){
			if(type.equals("task_name")){
				sql += " and task_id in (select id from task where name='" + val + "')";
			}
			if(type.equals("关键字")){
				sql += " and task_id in (select task_id from keytask where keyword_id in (select id from keyword "
						+ "where value='" + val + "'))";
			}
		}
		sql += " and task_id in (select id from task where type='" + advSearchPara.getTaskType() + "')";
		int curPage = 1;
		if(!advSearchPara.getCurPage().equals("")){
			curPage = Integer.valueOf(advSearchPara.getCurPage());
		}
		int totalNum = (int) resourceFileDao.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int pageSize = Integer.valueOf(advSearchPara.getPageSize());
		PageView<RESOURCEFILE> temp= new PageView<RESOURCEFILE>(pageSize);
		PageView<FilePrePara> pageView = new PageView<FilePrePara>(pageSize);
		pageView.setTotalNum(totalNum);
		temp.setTotalNum(totalNum);
		pageView.setCurPage(curPage);
		temp.setCurPage(curPage);
		resourceFileDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, RESOURCEFILE.class, temp);
		
		List<FilePrePara> items = new LinkedList<FilePrePara>();
		if(temp.getItems().size() < 1)
			return pageView;
		for(RESOURCEFILE rfile : temp.getItems()){
			FilePrePara item = new FilePrePara();
			item.setTitle(rfile.getTitle());
			item.setSrc(rfile.getRelative_path());
			item.setURL(rfile.getFile_url());
			item.setFileId(rfile.getId());
			List<KeyContext> keyContextList = new LinkedList<KeyContext>();
			String fileId = rfile.getId();
			String sqlc = "select * from context where resourcefile_id='" + fileId+ "'";
			@SuppressWarnings("unchecked")
			List<CONTEXT> contextList = (List<CONTEXT>) contextDao.findByQuery(sqlc, CONTEXT.class);
			for(CONTEXT context : contextList){
				KeyContext keyContext = new KeyContext();
				keyContext.setContext(filterBadHtmlTag(context.getText()));
				keyContext.setContext_id(context.getId());
				keyContext.setKey(context.getKeyword());

				keyContextList.add(keyContext);
			}
			FILESTATISTICS fileStatistics = fileStatisticsDao.findByFile_id(fileId);
			
			item.setIresult(fileStatistics.getSecret_result());
			item.setKeyContextList(keyContextList);
			item.setSecureCnt(fileStatistics.getSecret_num());
			item.setContainFileFlag(fileStatistics.getContain_file_flag());
			String filename = rfile.getRelative_path();
			String prefix= filename.substring(filename.lastIndexOf(".")+1);
			item.setType(prefix);
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
	}

	@Override
	public PageView<PagePrePara> getPageListByTaskId(String taskId, String curPage) {
		String sql = "select * from resourceFile where task_id='" + taskId + "' and (page_url=file_url or file_url is null) "
				+ "and id in (select resourcefile_id from filestatistics)";
		int totalNum = (int) resourceFileDao.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int curP = Integer.valueOf(curPage);
		PageView<RESOURCEFILE> temp= new PageView<RESOURCEFILE>(10);
		PageView<PagePrePara> pageView = new PageView<PagePrePara>(10);
		pageView.setTotalNum(totalNum);
		temp.setTotalNum(totalNum);
		pageView.setCurPage(curP);
		temp.setCurPage(curP);
		resourceFileDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, RESOURCEFILE.class, temp);
		List<PagePrePara> items = new LinkedList<PagePrePara>();
		if(temp.getItems().size() < 1)
			return pageView;
		for(RESOURCEFILE rfile : temp.getItems()){
			PagePrePara item = new PagePrePara();
			item.setPageId(rfile.getId());
			item.setTitle(rfile.getTitle());
			item.setSrc(rfile.getRelative_path()==null ? "正文未检出" : rfile.getRelative_path());
			item.setURL(rfile.getPage_url());
			List<KeyContext> keyContextList = new LinkedList<KeyContext>();
			String fileId = rfile.getId();
			String sqlc = "select * from context where resourcefile_id='" + fileId+ "'";
			@SuppressWarnings("unchecked")
			List<CONTEXT> contextList = (List<CONTEXT>) contextDao.findByQuery(sqlc, CONTEXT.class);
			for(CONTEXT context : contextList){
				KeyContext keyContext = new KeyContext();
				keyContext.setContext(filterBadHtmlTag(context.getText()));
				keyContext.setContext_id(context.getId());
				keyContext.setKey(context.getKeyword());

				keyContextList.add(keyContext);
			}
			FILESTATISTICS fileStatistics = fileStatisticsDao.findByFile_id(fileId);
			
			item.setIresult(fileStatistics.getSecret_result());
			item.setKeyContextList(keyContextList);
			item.setSecureCnt(fileStatistics.getSecret_num());
			item.setContainFileFlag(fileStatistics.getContain_file_flag());
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
	}

	@Override
	public PageView<FilePrePara> getFileListByPageId(String pageId, String curPage) {
		String sql = "select * from resourceFile where page_url in (select page_url from resourcefile where id ='" + pageId + "') "
				+ " and hashname is not NULL";
		int totalNum = (int) resourceFileDao.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return null;
		int curP = Integer.valueOf(curPage);
		PageView<RESOURCEFILE> temp= new PageView<RESOURCEFILE>(10);
		PageView<FilePrePara> pageView = new PageView<FilePrePara>(10);
		pageView.setTotalNum(totalNum);
		temp.setTotalNum(totalNum);
		pageView.setCurPage(curP);
		temp.setCurPage(curP);
		resourceFileDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, RESOURCEFILE.class, temp);
		List<FilePrePara> items = new LinkedList<FilePrePara>();
		if(temp.getItems().size() < 1)
			return pageView;
		for(RESOURCEFILE rfile : temp.getItems()){
			FilePrePara item = new FilePrePara();
			item.setFileId(rfile.getId());
			item.setTitle(rfile.getTitle());
			item.setSrc(rfile.getRelative_path());
			item.setURL(rfile.getFile_url());
			List<KeyContext> keyContextList = new LinkedList<KeyContext>();
			String fileId = rfile.getId();
			String sqlc = "select * from context where resourcefile_id='" + fileId+ "'";
			@SuppressWarnings("unchecked")
			List<CONTEXT> contextList = (List<CONTEXT>) contextDao.findByQuery(sqlc, CONTEXT.class);
			for(CONTEXT context : contextList){
				KeyContext keyContext = new KeyContext();
				keyContext.setContext(filterBadHtmlTag(context.getText()));
				keyContext.setContext_id(context.getId());
				keyContext.setKey(context.getKeyword());

				keyContextList.add(keyContext);
			}
			FILESTATISTICS fileStatistics = fileStatisticsDao.findByFile_id(fileId);
			
			item.setIresult(fileStatistics.getSecret_result());
			item.setKeyContextList(keyContextList);
			item.setSecureCnt(fileStatistics.getSecret_num());
			item.setContainFileFlag(fileStatistics.getContain_file_flag());
			String fileName = rfile.getRelative_path();
			String prefix= fileName.substring(fileName.lastIndexOf(".")+1);
			item.setType(prefix);
			items.add(item);
		}
		pageView.setItems(items);
		return pageView;
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
	@Override
	public JSONArray getTaskKeyword(String taskId) {
		// TODO Auto-generated method stub
		JSONArray keyArr = new JSONArray();
		String sql1 = "select * from keyword where id In (select keyword_id from keytask where task_id ='" + taskId + "')";
		String sql2 = "select * from keyword where id Not In (select keyword_id from keytask where task_id ='" + taskId + "')";
		@SuppressWarnings("unchecked")
		List<KEYWORD> keyList1 = (List<KEYWORD>) keywordDao.findByQuery(sql1, KEYWORD.class);
		@SuppressWarnings("unchecked")
		List<KEYWORD> keyList2 = (List<KEYWORD>) keywordDao.findByQuery(sql2, KEYWORD.class);
		
		for(KEYWORD keyword : keyList2){
			JSONObject obj = new JSONObject();
			String key = keyword.getValue();
			if(!keyArr.contains(obj)){
				obj.put("val", key);
				obj.put("checked", false);
			}
			keyArr.add(obj);
		}
		for(KEYWORD keyword : keyList1){
			JSONObject obj = new JSONObject();
			String key = keyword.getValue();
			
			obj.put("val", key);
			obj.put("checked", true);
			
			keyArr.add(obj);
		}
		return keyArr;
	}
	
	//获取网站信息列表，每个网站单独生成一个word报告
	@Override
	public void getReportData(OutputStream os, String taskId) {
		// TODO Auto-generated method stub
		
		List<TASK> tasklist = taskDAO.getTaskListByIdList(taskId);
		if(tasklist.size() <1 ) return;
		TASK task = tasklist.get(0);
			
		String id = task.getId();
		String name = task.getName();
		
		//taskMap是某网站的map
		Map<String, Object> taskMap = new HashMap<String, Object>();
		taskMap.put("siteNameT", name + "保密检查结果报告");
		taskMap.put("siteName", "网站名称：" + name);
		taskMap.put("siteUrl", task.getLocation());
		taskMap.put("checkTime", "检查时间："+ ConverStringUtil.convertLongTime(task.getCreate_time()) +"至" + ConverStringUtil.convertLongTime(task.getEnd_time()));
		
		//查询某网站的统计信息列表
		List<TASKSTATISTICS> taskStatisticsList = taskStatisticsDao.findByTask_id(id);
		if(taskStatisticsList.size() > 0){
			//taskStatisticsListMap 是某网站内 文件列表的map, 一个网站最多对应三条taskStatistics记录（网页、图片，附件）
			List<Map<String, Object>> taskStatisticsListMap = new ArrayList<Map<String, Object>>();
			for(TASKSTATISTICS taskStatistics : taskStatisticsList){
				Map<String, Object> taskStatisticsMap = new HashMap<String, Object>();
				taskStatisticsMap.put("fileType", taskStatistics.getType());
				taskStatisticsMap.put("securedNum", taskStatistics.getSecret_num() == null ? "0" : taskStatistics.getSecret_num());
				taskStatisticsMap.put("noSecuredNum", taskStatistics.getNo_secret_num() == null ? "0" : taskStatistics.getNo_secret_num());
				taskStatisticsMap.put("suspectNum", taskStatistics.getSuspect_num() == null ? "0" : taskStatistics.getSuspect_num());
				taskStatisticsMap.put("waitDealNum", taskStatistics.getNo_deal_num() == null ? "0" : taskStatistics.getNo_deal_num());
				taskStatisticsMap.put("totalNum", taskStatistics.getTotal_num() == null ? "0" : taskStatistics.getTotal_num());
				taskStatisticsListMap.add(taskStatisticsMap);
			}
			taskMap.put("table1", taskStatisticsListMap);
			
			
			List<RESOURCEFILE> secPageFileList = resourceFileDao.getCentainTypeFile(id, this.SECTYPE, this.PAGEFILE);
			if(secPageFileList.size() > 0){
				//涉密网页文件列表的map
				List<Map<String, Object>> secPageListMap = new ArrayList<Map<String, Object>>();
				
				for(RESOURCEFILE file : secPageFileList){
					Map<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("fileName", file.getTitle());
					fileMap.put("fileUrl", file.getFile_url());
					fileMap.put("filePath", file.getRelative_path());
					List<CONTEXT> contextList = contextDao.getContextByfileIdAndSecType(file.getId(), this.SECTYPE);
					String keys = "";
					String contexts = "";
					int i=1;
					for(CONTEXT context : contextList){
						keys += "第" + i + "条:" + context.getKeyword() + "; ";
						contexts += "第" + i + "条:" + context.getText() + "; ";
						i++;
					}
					if(keys.length() > 0){
						keys = keys.substring(0, keys.length() - 2);
					}
					if(contexts.length() > 0){
						contexts = contexts.substring(0, contexts.length() - 2);
					}
					fileMap.put("key", keys);
					contexts = this.filterInvalidMark(contexts);
					fileMap.put("context", contexts);
					
					secPageListMap.add(fileMap);
				}
				//装入涉密网页文件列表信息
				taskMap.put("table2", secPageListMap);
			}
			
			List<RESOURCEFILE> secPictureFileList = resourceFileDao.getCentainTypeFile(id, this.SECTYPE, this.PICTUREFILE);
			
			if(secPictureFileList.size() > 0){
				//涉密图片文件列表的map
				List<Map<String, Object>> secPictureListMap = new ArrayList<Map<String, Object>>();
				
				for(RESOURCEFILE file : secPictureFileList){
					Map<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("fileName", file.getTitle());
					fileMap.put("fileUrl", file.getFile_url());
					fileMap.put("filePath", file.getRelative_path());
					List<CONTEXT> contextList = contextDao.getContextByfileIdAndSecType(file.getId(), this.SECTYPE);
					String keys = "";
					String contexts = "";
					int i=1;
					for(CONTEXT context : contextList){
						keys += "第" + i + "条:" + context.getKeyword() + "; ";
						contexts += "第" + i + "条:" + context.getText() + "; ";
						i++;
					}
					if(keys.length() > 0){
						keys = keys.substring(0, keys.length() - 2);
					}
					if(contexts.length() > 0){
						contexts = contexts.substring(0, contexts.length() - 2);
					}
					fileMap.put("key", keys);
					fileMap.put("context", contexts);
					
					secPictureListMap.add(fileMap);
				}
				taskMap.put("table3", secPictureListMap);
			}
			
			List<RESOURCEFILE> secAttachFileList = resourceFileDao.getCentainTypeFile(id, this.SECTYPE, this.ATTACHFILE);
			if(secAttachFileList.size() > 0){
				//涉密附件文件列表的map
				List<Map<String, Object>> secAttachListMap = new ArrayList<Map<String, Object>>();
				
				for(RESOURCEFILE file : secAttachFileList){
					Map<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("fileName", file.getTitle());
					fileMap.put("fileUrl", file.getFile_url());
					fileMap.put("filePath", file.getRelative_path());
					List<CONTEXT> contextList = contextDao.getContextByfileIdAndSecType(file.getId(), this.SECTYPE);
					String keys = "";
					String contexts = "";
					int i=1;
					for(CONTEXT context : contextList){
						keys += "第" + i + "条:" + context.getKeyword() + "; ";
						contexts += "第" + i + "条:" + context.getText() + "; ";
						i++;
					}
					if(keys.length() > 0){
						keys = keys.substring(0, keys.length() - 2);
					}
					if(contexts.length() > 0){
						contexts = contexts.substring(0, contexts.length() - 2);
					}
					fileMap.put("key", keys);
					fileMap.put("context", contexts);
					
					secAttachListMap.add(fileMap);
				}
				taskMap.put("table4", secAttachListMap);
			}
		
			List<RESOURCEFILE> susPageFileList = resourceFileDao.getCentainTypeFile(id, this.SUSTYPE, this.PAGEFILE);
			if(susPageFileList.size() > 0){
				//可疑网页文件列表的map
				List<Map<String, Object>> susPageListMap = new ArrayList<Map<String, Object>>();
				
				for(RESOURCEFILE file : susPageFileList){
					Map<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("fileName", file.getTitle());
					fileMap.put("fileUrl", file.getFile_url());
					fileMap.put("filePath", file.getRelative_path());
					List<CONTEXT> contextList = contextDao.getContextByfileIdAndSecType(file.getId(), this.SUSTYPE);
					String keys = "";
					String contexts = "";
					int i=1;
					for(CONTEXT context : contextList){
						keys += "第" + i + "条:" + context.getKeyword() + "; ";
						contexts += "第" + i + "条:" + context.getText() + "; ";
						i++;
					}
					if(keys.length() > 0){
						keys = keys.substring(0, keys.length() - 2);
					}
					if(contexts.length() > 0){
						contexts = contexts.substring(0, contexts.length() - 2);
					}
					fileMap.put("key", keys);
					contexts = this.filterInvalidMark(contexts);
					fileMap.put("context", contexts);
					
					susPageListMap.add(fileMap);
				}
				taskMap.put("table5", susPageListMap);
			}
			
			List<RESOURCEFILE> susPictureFileList = resourceFileDao.getCentainTypeFile(id, this.SUSTYPE, this.PICTUREFILE);
			if(susPictureFileList.size() > 0){
				//可疑图片文件列表的map
				List<Map<String, Object>> susPictureListMap = new ArrayList<Map<String, Object>>();
				
				for(RESOURCEFILE file : susPictureFileList){
					Map<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("fileName", file.getTitle());
					fileMap.put("fileUrl", file.getFile_url());
					fileMap.put("filePath", file.getRelative_path());
					List<CONTEXT> contextList = contextDao.getContextByfileIdAndSecType(file.getId(), this.SUSTYPE);
					String keys = "";
					String contexts = "";
					int i=1;
					for(CONTEXT context : contextList){
						keys += "第" + i + "条:" + context.getKeyword() + "; ";
						contexts += "第" + i + "条:" + context.getText() + "; ";
						i++;
					}
					if(keys.length() > 0){
						keys = keys.substring(0, keys.length() - 2);
					}
					if(contexts.length() > 0){
						contexts = contexts.substring(0, contexts.length() - 2);
					}
					fileMap.put("key", keys);
					fileMap.put("context", contexts);
					susPictureListMap.add(fileMap);
				}
				taskMap.put("table6", susPictureListMap);
			}
			
			List<RESOURCEFILE> susAttachFileList = resourceFileDao.getCentainTypeFile(id, this.SUSTYPE, this.ATTACHFILE);
			if(susAttachFileList.size() > 0){
				//可疑附件文件列表的map
				List<Map<String, Object>> susAttachListMap = new ArrayList<Map<String, Object>>();
				
				for(RESOURCEFILE file : susAttachFileList){
					Map<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("fileName", file.getTitle());
					fileMap.put("fileUrl", file.getFile_url());
					fileMap.put("filePath", file.getRelative_path());
					List<CONTEXT> contextList = contextDao.getContextByfileIdAndSecType(file.getId(), this.SUSTYPE);
					String keys = "";
					String contexts = "";
					int i=1;
					for(CONTEXT context : contextList){
						keys += "第" + i + "条:" + context.getKeyword() + "; ";
						contexts += "第" + i + "条:" + context.getText() + "; ";
						i++;
					}
					if(keys.length() > 0){
						keys = keys.substring(0, keys.length() - 2);
					}
					if(contexts.length() > 0){
						contexts = contexts.substring(0, contexts.length() - 2);
					}
					fileMap.put("key", keys);
					fileMap.put("context", contexts);
					susAttachListMap.add(fileMap);
				}
				taskMap.put("table7", susAttachListMap);
			}
		}
		LoadWordTemplate lt = new LoadWordTemplate();
		lt.createWordHttp(taskMap, os);
	}

	private String filterInvalidMark(String content){
		String regxpForHtml = "&nbsp;|&nbs";
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
	@Override
	public List<KeyContext> keyContextList(String fileId) {
		List<KeyContext> keyContextList = new LinkedList<KeyContext>();
		RESOURCEFILE resourceFile = resourceFileDao.cascadedQueryBothById(fileId, (short)3, "asc");
		List<CONTEXT> contextList = resourceFile.getContextList();
		for(CONTEXT context : contextList){
			KeyContext keyContext = new KeyContext();
			keyContext.setContext(this.filterBadHtmlTag(context.getText()));
			keyContext.setContext_id(context.getId());
			keyContext.setKey(context.getKeyword());
			keyContext.setResult(context.getContextResultList().get(0).getSecret_result());
			keyContextList.add(keyContext);
		}
		return keyContextList;
	}
	
	@Override
	public String saveContextResult(String fileId, String contextResultList) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		//1.保存contextResult结果
		updateContextResult(map,contextResultList);
		
		//2.保存fileStatistics结果
		this.updateFileStaResult(map.get("secret"), map.get("suspect"), fileId);
		
		//3.保存pFileStatistics结果
		this.updatePFileStaResult(fileId);
		//此处添加判断：1.首次保存判定结果；2.更新判定结果。
		//4.保存taskStatistics结果
		this.updateTaskStatResult(fileId);
		
		return "ok";
	}
	
	private void updateContextResult(Map<String, Integer> map, String contextResultList){
		int secNum =0;
		int suspectNum =0;
		List<String> secIdList = new LinkedList<String>();
		List<String> susIdList = new LinkedList<String>();
		List<String> noSecIdList = new LinkedList<String>();
		JSONArray arr = JSONArray.fromObject(contextResultList);
		for(int i=0; i<arr.size(); i++){
			JSONObject obj = (JSONObject) arr.get(i);
			String id = obj.getString("id");
			String result = obj.getString("result");
			switch(result){
				case "yes" : secNum++; secIdList.add(id); break;
				case "no" : noSecIdList.add(id); break;
				case "suspect" : suspectNum++; susIdList.add(id); break;
			}
		}
		if(secIdList.size()>0){
			String secIdListForSql = "";
			String sqlSec = "update contextResult set secret_result = 'secret' where context_id in (";
			for(String secId : secIdList){
				secIdListForSql += "'" + secId + "',";
			}
			secIdListForSql = secIdListForSql.substring(0, secIdListForSql.length()-1);
			sqlSec += secIdListForSql + ")";
			
			contextResultDao.executeBySql(sqlSec);
		}
		
		if(susIdList.size()>0){
			String suspectIdListForSql = "";
			String sqlSuspect = "update contextResult set secret_result='suspect' where context_id in (";
			for(String suspectId : susIdList){
				suspectIdListForSql += "'" + suspectId  + "',";
			}
			suspectIdListForSql = suspectIdListForSql.substring(0, suspectIdListForSql.length()-1);
			sqlSuspect += suspectIdListForSql + ")";
			contextResultDao.executeBySql(sqlSuspect);
		}
		
		if(noSecIdList.size()>0){
			String noSecIdListForSql = "";
			String sqlNoSec = "update contextResult set secret_result='no_secret' where context_id in (";
			for(String noSecId : noSecIdList){
				noSecIdListForSql += "'" + noSecId + "',";
			}
			noSecIdListForSql = noSecIdListForSql.substring(0, noSecIdListForSql.length()-1);
			sqlNoSec += noSecIdListForSql + ")";
			contextResultDao.executeBySql(sqlNoSec);
		}
		map.put("secret", secNum);
		map.put("suspect", suspectNum);
	}

	private void updateFileStaResult(int secNum, int suspectNum, String fileId){

		String sql4 = "update fileStatistics set secret_num=" + secNum + ", suspect_num = "+ suspectNum + ", secret_result=" + (secNum>0?"'secret'":(suspectNum>0?"'suspect'":"'no_secret'")) + " where resourcefile_id ='" + fileId + "'";
		
		fileStatisticsDao.executeBySql(sql4);
	}
	
	private void updatePFileStaResult(String fileId){
		String sql = "select * from fileStatistics where resourcefile_id in "
						+ "(select id from resourcefile where page_url in"
						+ "(select page_url from resourcefile where id='" + fileId + "') and hashname is not null)";
		List<FILESTATISTICS> fileStaList = (List<FILESTATISTICS>) fileStatisticsDao.findByQuery(sql, FILESTATISTICS.class);		
		int secret_num =0;
		int suspect_num =0;
		for(FILESTATISTICS fileSta : fileStaList){
			if(fileSta.getSecret_num()>0){
				secret_num +=1;
			}
			if(fileSta.getSuspect_num()>0){
				suspect_num +=1;
			}
		}
		String sqlu = "update fileStatistics set secret_num=" + secret_num + ", suspect_num=" + suspect_num + ", secret_result="
					+ (secret_num>0?"'secret'":(suspect_num>0?"'suspect'":"'no_secret'")) + " where resourcefile_id in "
					+ "(select id from resourcefile where page_url in "
					+ "(select page_url from resourcefile where id ='" + fileId +"') and contain_file_flag='yes')";
		fileStatisticsDao.executeBySql(sqlu);
	
	}
	
	private void updateTaskStatResult(String fileId){
		String sql = "select task_id from resourcefile where page_url in"
				+ "(select page_url from resourcefile where id='"+fileId+"') and hashname is null";
		List<String> taskIdList = (List<String>) resourceFileDao.findByQueryForList(sql, String.class);
		String taskId = taskIdList.get(0);
		String sqlF = "select * from fileStatistics where resourcefile_id in "
					+ "(select id from resourcefile where task_id='" + taskId + "' and page_url=file_url or contain_file_flag ='yes')";
		int secret_num = 0;
		int suspect_num = 0;
		int no_secret_num = 0;
		int no_deal_num = 0;
		List<FILESTATISTICS> fileStaList = (List<FILESTATISTICS>) fileStatisticsDao.findByQuery(sqlF, FILESTATISTICS.class);
		for(FILESTATISTICS fileSta : fileStaList){
			if(fileSta.getSecret_num()>0){
				secret_num ++;
			}
			if(fileSta.getSuspect_num()>0){
				suspect_num ++;
			}
			if(fileSta.getSecret_result().equals("no_secret")){
				no_secret_num ++;
			}
			if(fileSta.getSecret_result().equals("wait")){
				no_deal_num ++;
			}
		}
		String sql5 = "update taskStatistics set secret_num = " + secret_num+ ", no_secret_num = " 
					+ no_secret_num + ", suspect_num = "+ suspect_num + ", no_deal_num= "
					+ no_deal_num + " where task_id ='" + taskId + "'";
		
		taskStatisticsDao.executeBySql(sql5);
		
		if(secret_num > 0){
			String sql6 = "update task set secret_num = " + secret_num + " where id ='" + taskId +"'";
			taskDAO.executeBySql(sql6);
		}
	}

	@Override
	public String getPageSecureResult(String pageId) {
		// TODO Auto-generated method stub
		String sql = "select secret_result,secret_num from filestatistics where resourcefile_id = '" + pageId + "'";
		@SuppressWarnings("unchecked")
		List<FILESTATISTICS> resultList = (List<FILESTATISTICS>) fileStatisticsDao.findByQuery(sql, FILESTATISTICS.class);
		JSONObject jObj = new JSONObject();
		jObj.put("secret_result", resultList.get(0).getSecret_result());
		jObj.put("secret_num",resultList.get(0).getSecret_num());
		
		return jObj.toString();
	}

	@Override
	public String getRSrcByPageId(String pageId) {
		
		return resourceFileDao.getRelativePath(pageId);
	}
}
