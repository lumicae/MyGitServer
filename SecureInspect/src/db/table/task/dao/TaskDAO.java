package db.table.task.dao;

import java.util.Date;
import java.util.List;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.task.bean.TASK;

public interface TaskDAO extends MainDAOPractice<TASK>{

	public int updateStatus(String status, String id);
	
	public int saveEndTm(Date time, int id);
	
	public TASK getEntityByName(String name);
	
	public List<TASK> getTaskListByIdList(String ids);
	
	public String getIdByName(String taskname);
}
