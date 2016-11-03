package mvc.keyword;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import logic.keyword.KeywordService;
import mvc.account.KeyInfo;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import utils.PageView;
import db.table.keyword.bean.KEYWORD;

@Controller
public class KeywordController {
	
	@Autowired
	@Qualifier("keywordService")
	private KeywordService keywordService;

	@RequestMapping("/keyword")
	public ModelAndView keyword(){
		ModelAndView mav = new ModelAndView();
		PageView<KEYWORD> pageView = new PageView<KEYWORD>(10);
		keywordService.findAllKeyword(pageView, 1);
		mav.addObject("pageview", pageView);
		mav.setViewName("keyword");
		return mav;
	}
	
	@RequestMapping("keyword0/{curPage}")
	public ModelAndView keyword0(@PathVariable("curPage") int curPage){
		ModelAndView mav = new ModelAndView();
		PageView<KEYWORD> pageview = new PageView<KEYWORD>(10);
		keywordService.findAllKeyword(pageview, curPage);
		mav.addObject("pageview", pageview);
		mav.setViewName("keyword0");
		return mav;
	}
	
	@RequestMapping("/addKey")
	public void addKey(@ModelAttribute("keyword") KEYWORD keyword, HttpServletResponse response){
		
		KeyInfo keyInfo = new KeyInfo();
		
		keyInfo.setFlag(keywordService.saveKeyword(keyword)?"ok" : "no");
		keyInfo.setRank(keyword.getRank());
		keyInfo.setValue(keyword.getValue());
		JSONObject json = JSONObject.fromObject(keyInfo); 
		try {
			response.setContentType("text/html;charset=UTF-8");
			String command = "window.parent.callbackInfo('" + json.toString() + "');";
			response.getWriter().print("<script type='text/javascript'>"+command+"</script>");
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
