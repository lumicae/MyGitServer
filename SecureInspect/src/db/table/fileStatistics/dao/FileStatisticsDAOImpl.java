package db.table.fileStatistics.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import db.DAO.mainDAOPractice.MainDAOPracticeImpl;
import db.table.fileStatistics.bean.FILESTATISTICS;

@Repository("fileStatisticsDao")
public class FileStatisticsDAOImpl  extends MainDAOPracticeImpl<FILESTATISTICS> implements FileStatisticsDAO {

	@Override
	public FILESTATISTICS findByFile_id(String file_id) {
		String sql = "select * from fileStatistics where resourcefile_id = '" + file_id + "'";
		@SuppressWarnings("unchecked")
		List<FILESTATISTICS> fileStatisticsList = (List<FILESTATISTICS>) this.findByQuery(sql, FILESTATISTICS.class);
		return fileStatisticsList.size() > 0 ? fileStatisticsList.get(0) : null;
	}
}
