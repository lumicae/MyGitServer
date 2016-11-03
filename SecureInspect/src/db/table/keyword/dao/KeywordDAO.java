package db.table.keyword.dao;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.keyword.bean.KEYWORD;

public interface KeywordDAO  extends MainDAOPractice<KEYWORD>{

	public String getIdByWord(String key);
	
	public String getKwdsByTaskId(String siteId);

}
