package mvc.site;


public class AdvSearchPara {
	
	private String briefSrchKey;
	
	private String briefSrchVal;

	private String startTime;
	
	private String endTime;
	
	private String fileName;
	
	private String location;
	
	private String taskType;
	
	//¼ø¶¨½á¹û
	private String secCondition;
	
	private String pageSize;
	
	private String curPage;

	public String getBriefSrchKey() {
		return briefSrchKey;
	}

	public void setBriefSrchKey(String briefSrchKey) {
		this.briefSrchKey = briefSrchKey;
	}

	public String getBriefSrchVal() {
		return briefSrchVal;
	}

	public void setBriefSrchVal(String briefSrchVal) {
		this.briefSrchVal = briefSrchVal;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String Location) {
		this.location = Location;
	}

	public String getSecCondition() {
		return secCondition;
	}

	public void setSecCondition(String secCondition) {
		this.secCondition = secCondition;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
}
