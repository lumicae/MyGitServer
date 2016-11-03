package db.table.fileStatistics.bean;

import db.bean.mainBeanPractice.MainBeanPractice;
import db.bean.mainBeanPractice.MainBeanPracticeImpl;

public class FILESTATISTICS  extends MainBeanPracticeImpl implements MainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7364340242787378385L;
	
	private String id;
	
	private String resourcefile_id;
	
	private String secret_result;
	
	private String contain_file_flag; 
	
	private int total_num;
	
	private int secret_num;
	
	private int suspect_num;
	
	private long create_time;

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getResourcefile_id() {
		return resourcefile_id;
	}

	public void setResourcefile_id(String resourcefile_id) {
		this.resourcefile_id = resourcefile_id;
	}

	public String getSecret_result() {
		return secret_result;
	}

	public void setSecret_result(String secret_result) {
		this.secret_result = secret_result;
	}

	public String getContain_file_flag() {
		return contain_file_flag;
	}

	public void setContain_file_flag(String contain_file_flag) {
		this.contain_file_flag = contain_file_flag;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getSecret_num() {
		return secret_num;
	}

	public void setSecret_num(int secret_num) {
		this.secret_num = secret_num;
	}

	public int getSuspect_num() {
		return suspect_num;
	}

	public void setSuspect_num(int suspect_num) {
		this.suspect_num = suspect_num;
	}

	public String getId() {
		return id;
	}
}
