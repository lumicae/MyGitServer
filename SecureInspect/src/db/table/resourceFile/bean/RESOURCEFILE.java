package db.table.resourceFile.bean;

import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.NotNull;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;
import db.table.context.bean.CONTEXT;
import db.table.fileStatistics.bean.FILESTATISTICS;

/*资源文件表*/
public class RESOURCEFILE extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8980433446842145144L;

	/**
	 * 主键，哈希值，固定长度64位，非空
	 */
	@NotNull
	private String id;
	
	private String hashname;
	
	/**
	 * 外键
	 */
	@NotNull
	private String task_id;
	

	private String page_url;
	
	private String title;
	
	private String file_url;
	
	private String relative_path;
	
	private String type;
	
	private long create_time;

	private List<CONTEXT> contextList = new LinkedList<CONTEXT>();
	
	private List<FILESTATISTICS> fileStatisticsList = new LinkedList<FILESTATISTICS>();
	
	public List<CONTEXT> getContextList() {
		return contextList;
	}

	public void setContextList(List<CONTEXT> contextList) {
		this.contextList = contextList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getPage_url() {
		return page_url;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getRelative_path() {
		return relative_path;
	}

	public void setRelative_path(String relative_path) {
		this.relative_path = relative_path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public List<FILESTATISTICS> getFileStatisticsList() {
		return fileStatisticsList;
	}

	public void setFileStatisticsList(List<FILESTATISTICS> fileStatisticsList) {
		this.fileStatisticsList = fileStatisticsList;
	}

	public String getHashname() {
		return hashname;
	}

	public void setHashname(String hashname) {
		this.hashname = hashname;
	}
}
