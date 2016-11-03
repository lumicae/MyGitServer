package db.table.fileStatistics.dao;

import db.DAO.mainDAOPractice.MainDAOPractice;
import db.table.fileStatistics.bean.FILESTATISTICS;

public interface FileStatisticsDAO  extends MainDAOPractice<FILESTATISTICS>{
	public FILESTATISTICS findByFile_id(String file_id);
}
