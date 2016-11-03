package db.table.taskStatistics.bean;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;

public class TASKSTATISTICS extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -126348362825099947L;

	private String id;
	
	private String task_id;
	
	//filetype £º Í¼Æ¬ ÕýÎÄ ¸½¼þ
	private String type;
	
	private String secret_num = "0";
	
	private String no_secret_num = "0";
	
	private String suspect_num = "0";
	
	private String no_deal_num = "0";
	
	private String total_num = "0";
	
	private long create_time;
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSecret_num() {
		return secret_num;
	}

	public void setSecret_num(String secret_num) {
		this.secret_num = secret_num;
	}

	public String getNo_secret_num() {
		return no_secret_num;
	}

	public void setNo_secret_num(String no_secret_num) {
		this.no_secret_num = no_secret_num;
	}

	public String getSuspect_num() {
		return suspect_num;
	}

	public void setSuspect_num(String suspect_num) {
		this.suspect_num = suspect_num;
	}

	public String getNo_deal_num() {
		return no_deal_num;
	}

	public void setNo_deal_num(String no_deal_num) {
		this.no_deal_num = no_deal_num;
	}

	public String getTotal_num() {
		return total_num;
	}

	public void setTotal_num(String total_num) {
		this.total_num = total_num;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

}
