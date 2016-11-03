package db.table.contextResult.bean;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;

public class CONTEXTRESULT extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2633839030010740053L;

	private String id;
	
	private String user_id;
	
	private String secret_result;
	
	private String context_id;
	
	private long create_time;
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSecret_result() {
		return secret_result;
	}

	public void setSecret_result(String secret_result) {
		this.secret_result = secret_result;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getContext_id() {
		return context_id;
	}

	public void setContext_id(String context_id) {
		this.context_id = context_id;
	}

}
