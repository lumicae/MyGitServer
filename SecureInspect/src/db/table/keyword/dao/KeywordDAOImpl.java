package db.table.keyword.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.keyword.bean.KEYWORD;

@Repository("keywordDao")
public class KeywordDAOImpl extends MainDAOPracticeImpl<KEYWORD> implements KeywordDAO {

	@Override
	public String getIdByWord(String key) {
		String sql = "select id from keyword where value='" + key + "'";
		@SuppressWarnings("unchecked")
		List<String> idList = (List<String>) this.findByQueryForList(sql, String.class);
		return idList.size()>0 ? idList.get(0) : "";
	}

	@Override
	public String getKwdsByTaskId(String taskId) {
		String sql = "select value from keyword where id in ("
				+ "select keyword_id from keytask where task_id = '" + taskId + "')";
		@SuppressWarnings("unchecked")
		List<String> kwdList = (List<String>) this.findByQueryForList(sql, String.class);
		String kwds = "";
		if(kwdList.size() > 0){
			for(String kwd : kwdList){
				kwds += kwd + ",";
			}
			kwds = kwds.substring(0, kwds.length()-1);
		}
		
		return kwds;
	}

}
