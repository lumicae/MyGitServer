package db.table.taskStatistics.dao;

import java.util.List;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.taskStatistics.bean.TASKSTATISTICS;

public interface TaskStatisticsDAO extends MainDAOPractice<TASKSTATISTICS>{
	
	public List<TASKSTATISTICS> findByTask_id(String taskId);
	public TASKSTATISTICS findByTaskIdAndType(String taskId, String type);

}
