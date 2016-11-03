package db.table.context.dao;

import java.util.List;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.context.bean.CONTEXT;

public interface ContextDAO  extends MainDAOPractice<CONTEXT>{
	
	public List<CONTEXT> getContextByfileIdAndSecType(String file_id, String secType);
}
