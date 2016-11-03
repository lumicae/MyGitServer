package db.table.resourceFile.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.resourceFile.bean.RESOURCEFILE;

@Repository("resourceFileDao")
public class ResourceFileDAOImpl  extends MainDAOPracticeImpl<RESOURCEFILE> implements ResourceFileDAO {

	@Override
	public List<RESOURCEFILE> getCentainTypeFile(String taskId, String secType,
			String fileType) {
		// TODO Auto-generated method stub
		String colname = "suspect_num";
		if(secType == "secret") colname = "secret_num";
		String sql = "select * from resourcefile where id in(select resourcefile_id from filestatistics"
				+ " where " + colname + ">0) and type='" + fileType + "' and task_id ='" + taskId + "'";
		@SuppressWarnings("unchecked")
		List<RESOURCEFILE> fileList = (List<RESOURCEFILE>) this.findByQuery(sql, RESOURCEFILE.class);
		return fileList;
	}

	@Override
	public String getRelativePath(String pageId) {
		String sql = "select relative_path from resourcefile where id='" + pageId + "'";
		List<String> pathList = (List<String>) this.findByQueryForList(sql, String.class);
		
		return pathList.size()>0?pathList.get(0):"";
	}

}
