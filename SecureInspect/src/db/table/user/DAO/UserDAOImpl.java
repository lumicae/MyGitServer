package db.table.user.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.user.bean.USER;

@Repository("userDao")
public class UserDAOImpl extends MainDAOPracticeImpl<USER> implements UserDAO {

	@Override
	public boolean valid(String usr, String psd) {
		// TODO Auto-generated method stub
		String sql = "select * from user where username='" + usr + "' and password='" + psd + "'";
		@SuppressWarnings("unchecked")
		List<USER> userList = (List<USER>) this.findByQuery(sql, USER.class);
		return userList.size()>0 ? true : false;
	}
	
	@Override
	public boolean IsUserExisted(String username) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from user where username='" + username + "'";
		int t = (int) this.findByQueryForList(sql, Integer.class).get(0);
		return t>0 ? true : false;
	}
}
