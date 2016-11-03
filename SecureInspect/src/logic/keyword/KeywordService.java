package logic.keyword;

import java.util.List;

import utils.PageView;
import db.table.keyword.bean.KEYWORD;

public interface KeywordService {
	
	public boolean saveKeyword(KEYWORD keyword);
	
	public List<KEYWORD> findAllKeyword();
	
	public void findAllKeyword(PageView<KEYWORD> pageview, int curPage);

}
