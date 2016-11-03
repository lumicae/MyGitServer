package db.table.keyTask.dao;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.keyTask.bean.KEYTASK;

@Repository("keyTaskDao")
public class KeyTaskDaoImpl extends  MainDAOPracticeImpl<KEYTASK> implements KeyTaskDao {

	
}
