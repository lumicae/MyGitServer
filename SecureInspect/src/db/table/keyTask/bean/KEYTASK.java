package db.table.keyTask.bean;

import javax.validation.constraints.NotNull;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;

public class KEYTASK extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215171997116725708L;

	@NotNull
	private String id;
	
	@NotNull
	private String keyword_id;
	
	@NotNull
	private String task_id;
	
	private long create_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyword_id() {
		return keyword_id;
	}

	public void setKeyword_id(String keyword_id) {
		this.keyword_id = keyword_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
}
