package db.table.task.bean;

import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.NotNull;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;
import db.table.keyTask.bean.KEYTASK;
import db.table.resourceFile.bean.RESOURCEFILE;
import db.table.taskStatistics.bean.TASKSTATISTICS;

public class TASK extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1117407993497300559L;

	/**
	 * 主键，非空，固定长度32位
	 */
	@NotNull
	private String id;
	
	/**
	 * 外键，非空，固定长度32位
	 */
	@NotNull
	private String user_id;
	
	private String location;
	
	private String name;
	
	private String status;
	
	private long create_time;
	
	private long end_time;
	
	private int total;
	
	private int secret_num;
	
	private String type;
	
	private List<RESOURCEFILE> pageList = new LinkedList<RESOURCEFILE>();

	private List<KEYTASK> keyTaskList = new LinkedList<KEYTASK>();
	
	private List<TASKSTATISTICS> taskStatistics = new LinkedList<TASKSTATISTICS>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String User_id) {
		this.user_id = User_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSecret_num() {
		return secret_num;
	}

	public void setSecret_num(int secret_num) {
		this.secret_num = secret_num;
	}
	
	public List<RESOURCEFILE> getPageList(){
		return pageList;
	}
	public void setPageList(List<RESOURCEFILE> pageList){
		this.pageList = pageList;
	}
	
	public List<KEYTASK> getKeyTaskList(){
		return keyTaskList;
	}
	
	public void setKeyTaskList(List<KEYTASK> keyTaskList){
		this.keyTaskList = keyTaskList;
	}

	public List<TASKSTATISTICS> getTaskStatistics() {
		return taskStatistics;
	}

	public void setTaskStatistics(List<TASKSTATISTICS> taskStatistics) {
		this.taskStatistics = taskStatistics;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
