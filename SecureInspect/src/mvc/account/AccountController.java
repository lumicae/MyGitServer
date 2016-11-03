package mvc.account;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logic.account.UserService;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import db.table.user.bean.USER;

@Controller
public class AccountController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@RequestMapping("/checkLoginInfo")
	public void checkLoginInfo(LoginPara loginPara, HttpServletResponse response, HttpSession session){
		session.removeAttribute("username");
		boolean t = userService.validUserInfo(loginPara, session);
		if(t){
			session.setAttribute("username", loginPara.getUsername());
			try {
				response.sendRedirect("/SecureInspect/account/mainPage");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{ 
			try {
				JSONObject obj = new JSONObject();
				obj.put("flag", "failed");
				response.setContentType("text/html;charset=UTF-8");
				String command = "window.parent.loginCallback('" + obj.toString() + "');";
				response.getWriter().print("<script type='text/javascript'>"+command+"</script>");
				response.getWriter().print(obj.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping("/user")
	public ModelAndView login(HttpSession session){
		//Í¨¹ýsession¼ÇÂ¼µÇÂ¼×´Ì¬
		ModelAndView mav = new ModelAndView();
		String username = (String) session.getAttribute("username");
		if(username != null && !"".endsWith(username)){
			mav.addObject("username", session.getAttribute("username"));
			mav.setViewName("user");
		}
		else{
			mav.setViewName("redirect:/");
		}
		return mav;
	}
	
	@RequestMapping("/saveUserInfo")
	public ModelAndView register(@ModelAttribute("user") USER user, HttpServletRequest request, HttpSession session){
		ModelAndView mav = new ModelAndView();
		String t = userService.registerUser(user);
		session.setAttribute("username", user.getUsername());
		if(t.equals("failed")){
			mav.addObject("flag", "µÇÂ¼Ê§°Ü");
			mav.setViewName("redirect:/");
		}
		else{
			mav.setViewName("mainPage");
		}
		return mav;
	}
	
	@RequestMapping("/checkSaveRegisterInfo")
	public void checkSaveRegisterInfo(@ModelAttribute("user") USER user, BindingResult bindingResult, HttpServletResponse response, HttpSession session){
		session.removeAttribute("username");
		JSONObject obj = new JSONObject();
		obj.put("flag", "failed");
		obj.put("username", user.getUsername());
		obj.put("password", user.getPassword());
		if(!bindingResult.hasErrors()){
			String t = userService.registerUser(user);
			session.setAttribute("username", user.getUsername());
			obj.put("flag", t);
		}
		
		try {
			response.setContentType("text/html;charset=UTF-8");
			String command = "window.parent.callbackInfo('" + obj.toString() + "');";
			response.getWriter().print("<script type='text/javascript'>"+command+"</script>");
			response.getWriter().print(obj.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/updatePwd")
	public void updatePwd(String obj, HttpServletResponse response, HttpSession session){
		String username = (String) session.getAttribute("username");
		String res = "unlogin";
		if(username != null && !username.equals("")){
			boolean flag = userService.updatePwd(obj, session);
			if(flag){
				res = "ok";
			}
			else{
				res = "no";
			}
		}
		try {
			response.getWriter().print(res);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/mainPage")
	public ModelAndView mainPage(){
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("mainPage");
		return mav;
	}
	
	// ÑéÖ¤Âë
	@RequestMapping("/checkNumberShow")
	public ModelAndView checkNumberShow() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("checkNumber");
		return mav;
	}
	
	@RequestMapping("/help")
	public ModelAndView help(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("help");
		return mav;
	}
}
