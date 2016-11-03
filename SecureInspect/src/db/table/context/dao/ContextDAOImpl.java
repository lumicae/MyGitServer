package db.table.context.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.context.bean.CONTEXT;

@Repository("contextDao")
public class ContextDAOImpl extends MainDAOPracticeImpl<CONTEXT> implements ContextDAO {

	@Override
	public List<CONTEXT> getContextByfileIdAndSecType(String file_id,
			String secType) {
		// TODO Auto-generated method stub
		String sql = "select * from context where resourcefile_id = '" + file_id + "'"
						+ " and secret_result='" + secType + "'";
		@SuppressWarnings("unchecked")
		List<CONTEXT> contextList = (List<CONTEXT>) this.findByQuery(sql, CONTEXT.class);
		return contextList;
	}

}
