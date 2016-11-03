package logic.account;

import javax.servlet.http.HttpSession;

import mvc.account.LoginPara;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import db.table.user.DAO.UserDAO;
import db.table.user.bean.USER;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("userDao")
	private UserDAO userDao;
	
	
	@Override
	public boolean validUserInfo(LoginPara loginPara, HttpSession session) {
		// TODO Auto-generated method stub
		boolean flag1 = userDao.valid(loginPara.getUsername(), loginPara.getPassword());
		
		boolean flag2 = check(loginPara.getCheckCode(), session);
		
		return flag1 && flag2;
	}
	
	private boolean check(String validateCode, HttpSession session){
    	//从session中获取正确的验证码
    	String sessionValidateCode = "";
    	Object obj = session.getAttribute("SESSION_VALIDATE_CODE");
    	if(obj != null){
    		sessionValidateCode = obj.toString();
    		if(sessionValidateCode.equalsIgnoreCase(validateCode))
    			return true;
    		else
    			return false;
    	}
    	else
    		return false;	
	}
	
	@Override
	public String registerUser(USER user) {
		// TODO Auto-generated method stub	
		int t = 0;
		if(!userDao.IsUserExisted(user.getUsername())){
			t = userDao.save(user);
		}
		return t>0 ? "ok" : "failed";
	}

	@Override
	public boolean updatePwd(String obj, HttpSession session) {
		// TODO Auto-generated method stub
		JSONObject json = JSONObject.fromObject(obj);
		String oldPwd = json.getString("oldPwd");
		String newPwd = json.getString("newPwd");
		String confirPwd = json.getString("confirPwd");
		String username = (String) session.getAttribute("username");
		if(newPwd.equals(confirPwd) && !newPwd.equals(oldPwd)){
			//1.验证oldPwd是否正确
			//2.保存newPwd
			String sql1 = "select password from user where username='" + username + "'";
			String tempPwd = (String) userDao.findByQueryForList(sql1, String.class).get(0);
			if(tempPwd.equals(oldPwd)){
				String sql2 = "update user set password='" + newPwd + "'";
				userDao.executeBySql(sql2);
				return true;
			}
		}
		return false;
	}
}
