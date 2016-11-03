package mvc.account;


public class TaskPara {
	
	private String taskId;

	private String taskName;
	
	private String location;
	
	private String kwds;
	
	private String time;
	
	private String status;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getKwds() {
		return kwds;
	}

	public void setKwds(String kwds) {
		this.kwds = kwds;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
