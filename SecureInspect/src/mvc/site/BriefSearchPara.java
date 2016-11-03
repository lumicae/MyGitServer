package mvc.site;

public class BriefSearchPara {

	private String BriefSrchKey;
	
	private String BriefSrchVal;
	
	private String pageSize;
	
	private String curPage;
	
	private String taskType;

	public String getBriefSrchKey() {
		return BriefSrchKey;
	}

	public void setBriefSrchKey(String briefSrchKey) {
		BriefSrchKey = briefSrchKey;
	}

	public String getBriefSrchVal() {
		return BriefSrchVal;
	}

	public void setBriefSrchVal(String briefSrchVal) {
		BriefSrchVal = briefSrchVal;
	}

	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
}
