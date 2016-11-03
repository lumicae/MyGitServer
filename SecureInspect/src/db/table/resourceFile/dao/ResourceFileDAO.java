package db.table.resourceFile.dao;

import java.util.List;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.resourceFile.bean.RESOURCEFILE;

public interface ResourceFileDAO  extends MainDAOPractice<RESOURCEFILE>{

	/**
	 * 
	 * @param taskId
	 * @param secType ��ȫ���ͣ����ܡ�����
	 * @param fileType �ļ����ͣ�ͼƬ����ҳ������
	 * @return
	 */
	public List<RESOURCEFILE> getCentainTypeFile(String taskId, String secType, String fileType);
	
	public String getRelativePath(String pageId); 
	
}
