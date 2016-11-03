package db.table.contextResult.dao;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.contextResult.bean.CONTEXTRESULT;

@Repository("contextResultDao")
public class ContextResultDAOImpl extends MainDAOPracticeImpl<CONTEXTRESULT> implements ContextResultDAO {

}
