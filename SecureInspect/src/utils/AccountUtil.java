package utils;

import javax.servlet.http.HttpSession;

public class AccountUtil {

	public boolean isAnyAccountInLoggedState(HttpSession session){
		String username = (String) session.getAttribute("username");
		if("admin".equals(username)){
			return true;
		}
		else{
			return false;
		}
	}
}
