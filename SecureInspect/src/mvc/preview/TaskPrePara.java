package mvc.preview;

import java.util.Date;

public class TaskPrePara {
	
	private String taskId;
	
	private String name;
	
	private String location;
	
	private Date startTime;
	
	private Date endTime;
	
	private int secureCnt;
	
	private int total;
	
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getSecureCnt() {
		return secureCnt;
	}

	public void setSecureCnt(int secureCnt) {
		this.secureCnt = secureCnt;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
