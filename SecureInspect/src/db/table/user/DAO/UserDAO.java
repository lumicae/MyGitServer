package db.table.user.DAO;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.user.bean.USER;

public interface UserDAO extends MainDAOPractice<USER>{
	
	public boolean valid(String usr, String psd);
	
	public boolean IsUserExisted(String username);
}
