package db.table.taskStatistics.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.taskStatistics.bean.TASKSTATISTICS;

@Repository("taskStatisticsDao")
public class TaskStatisticsDAOImpl extends MainDAOPracticeImpl<TASKSTATISTICS> implements TaskStatisticsDAO {

	@Override
	public List<TASKSTATISTICS> findByTask_id(String taskId) {
		// TODO Auto-generated method stub
		String sql = "select * from taskStatistics where task_id = '" + taskId + "'";
		@SuppressWarnings("unchecked")
		List<TASKSTATISTICS> taskStatisticsList = (List<TASKSTATISTICS>) this.findByQuery(sql, TASKSTATISTICS.class);
		return taskStatisticsList;
	}

	@Override
	public TASKSTATISTICS findByTaskIdAndType(String taskId, String type) {
		// TODO Auto-generated method stub
		String sql = "select * from taskStatistics where task_id = '" + taskId + "' and type ='" + type + "'";
		@SuppressWarnings("unchecked")
		List<TASKSTATISTICS> taskStatisticsList = (List<TASKSTATISTICS>) this.findByQuery(sql, TASKSTATISTICS.class);
		return taskStatisticsList.size() > 0 ? taskStatisticsList.get(0) : null;
	}

}
