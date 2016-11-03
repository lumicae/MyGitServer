package mvc.task;

//2016年9月2日
//用于向redis中传递json格式的字符串
public class TaskInfoPara {

	private String kwds;
	
	private String sitename;
	
	private String siteurl;
	
	private String type;
	
	private String dirname;
	
	private String taskid;

	public String getKwds() {
		return kwds;
	}

	public void setKwds(String kwds) {
		this.kwds = kwds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDirname() {
		return dirname;
	}

	public void setDirname(String dirname) {
		this.dirname = dirname;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getSiteurl() {
		return siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
