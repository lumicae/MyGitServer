package db.table.context.bean;

import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.NotNull;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;
import db.table.contextResult.bean.CONTEXTRESULT;

public class CONTEXT extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8201749393559399772L;

	/**
	 * 主键，非空，固定长度32位
	 */

	private String id;
	
	/**
	 * 外键，非空，固定长度64位
	 */
	@NotNull
	private String resourcefile_id;
	
	private String keyword;
	
	private String text;
	
	private long create_time;
	
	private List<CONTEXTRESULT> contextResultList = new LinkedList<CONTEXTRESULT>();

	public String getResourcefile_id() {
		return resourcefile_id;
	}
	public void setResourcefile_id(String resourcefile_id) {
		this.resourcefile_id = resourcefile_id;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getId() {
		return id;
	}
	public List<CONTEXTRESULT> getContextResultList() {
		return contextResultList;
	}
	public void setContextResultList(List<CONTEXTRESULT> contextResultList) {
		this.contextResultList = contextResultList;
	}

}
