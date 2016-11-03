package db.table.task.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.task.bean.TASK;

@Repository("taskDao")
public class TaskDAOImpl extends MainDAOPracticeImpl<TASK> implements TaskDAO {
	
	

	@Override
	public int updateStatus(String status, String id) {
		// TODO Auto-generated method stub
		long t = System.currentTimeMillis();
		String sql = "update task set status='" + status + "',end_time=" + t + " where id='" + id + "'" ;
		this.executeBySql(sql);
		return 0;
	}

	@Override
	public int saveEndTm(Date time, int id) {
		// TODO Auto-generated method stub
		long l = time.getTime();
		Timestamp end_time = new Timestamp(l);
		String sql = "update task set end_time='" + end_time + "' where id=" + id;
		this.executeBySql(sql);
		return 0;
	}

	@Override
	public TASK getEntityByName(String name) {
		// TODO Auto-generated method stub
		String sql = "select * from task where name='" + name +"'";
		TASK task = (TASK) this.findByQuery(sql, TASK.class).get(0);
		return task;
	}

	@Override
	public List<TASK> getTaskListByIdList(String ids) {
		// TODO Auto-generated method stub
		String sql = "select * from task where id in ('" + ids + "')";
		@SuppressWarnings("unchecked")
		List<TASK> taskList = (List<TASK>) this.findByQuery(sql, TASK.class);
		return taskList;
	}

	@Override
	public String getIdByName(String taskname) {
		String sql = "select id from task where name='" + taskname + "' order by create_time desc";
		@SuppressWarnings("unchecked")
		List<String> idList = (List<String>) this.findByQueryForList(sql, String.class);
		
		return idList!=null ? idList.get(0) : "";
	}
}
