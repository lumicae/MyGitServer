package db.table.keyword.bean;

import com.sun.istack.internal.NotNull;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;

public class KEYWORD extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1107560053891789845L;

	/**
	 * 主键，非空，固定长度32位
	 */
	@NotNull
	private String id;
	
	/**
	 * 外键，非空，固定长度32位
	 */
	@NotNull
	private String user_id = "1";
	
	private String rank;
	
	private String value;
	
	private long create_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
}
