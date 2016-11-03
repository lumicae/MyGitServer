package logic.keyword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import utils.PageView;
import db.table.keyword.bean.KEYWORD;
import db.table.keyword.dao.KeywordDAO;

@Service("keywordService")
public class KeywordServiceImpl implements KeywordService {

	
	@Autowired
	@Qualifier("keywordDao")
	private KeywordDAO keywordDao;
	
	@Override
	public boolean saveKeyword(KEYWORD keyword) {
		keyword.setCreate_time(System.currentTimeMillis());
		return keywordDao.save(keyword) > 0 ? true : false;
	}

	/**
	 * 为关键字管理页面提供数据
	 */
	@Override
	public List<KEYWORD> findAllKeyword() {
		String sql = "select * from keyword order by create_time desc;";
		@SuppressWarnings("unchecked")
		List<KEYWORD> keywordList = (List<KEYWORD>) keywordDao.findByQuery(sql, KEYWORD.class);
		return keywordList;
	}


	@Override
	public void findAllKeyword(PageView<KEYWORD> pageview, int curPage) {
		String sql = "select * from keyword";
		int totalNum = (int) keywordDao.getTotalItemsNumBySelectQuery(sql);
		if(totalNum < 1) return;
		pageview.setTotalNum(totalNum);
		pageview.setCurPage(curPage);
		keywordDao.pagingFindBySelectQueryAndSortByRownumForSingleTable(sql, KEYWORD.class, pageview);
	}
	
}
