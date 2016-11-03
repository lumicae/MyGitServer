package db.table.resourceFile.dao;

import java.util.List;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.resourceFile.bean.RESOURCEFILE;

public interface ResourceFileDAO  extends MainDAOPractice<RESOURCEFILE>{

	/**
	 * 
	 * @param taskId
	 * @param secType 安全类型：涉密、可疑
	 * @param fileType 文件类型：图片、网页、附件
	 * @return
	 */
	public List<RESOURCEFILE> getCentainTypeFile(String taskId, String secType, String fileType);
	
	public String getRelativePath(String pageId); 
	
}
