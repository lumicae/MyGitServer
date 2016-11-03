package db.table.user.bean;

import com.sun.istack.internal.NotNull;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;

public class USER extends MainBeanPracticeImpl implements MainBeanPractice {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4033322725821820415L;

	/**
	 * 主键，非空，固定长度32位
	 */
	@NotNull
	private String id;
	
	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	private long create_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
}
