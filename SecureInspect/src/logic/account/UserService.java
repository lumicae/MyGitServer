package logic.account;

import javax.servlet.http.HttpSession;

import mvc.account.LoginPara;
import db.table.user.bean.USER;

public interface UserService {

	public boolean validUserInfo(LoginPara loginPara, HttpSession session);
	
	public String registerUser(USER user);

	public boolean updatePwd(String obj, HttpSession session);
}
