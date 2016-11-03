package mvc.result;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import logic.result.ResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import db.table.context.dao.ContextDAO;
import db.table.fileStatistics.bean.FILESTATISTICS;
import db.table.fileStatistics.dao.FileStatisticsDAO;
import db.table.resourceFile.bean.RESOURCEFILE;
import db.table.resourceFile.dao.ResourceFileDAO;

@Controller
public class ResultController {
	
	@Autowired
	@Qualifier("resultService")
	private ResultService resultService;
	
	@Autowired
	@Qualifier("contextDao")
	private ContextDAO contextDao;
	
	@Autowired
	@Qualifier("resourceFileDao")
	private ResourceFileDAO resourceFileDao;
	
	@Autowired
	@Qualifier("fileStatisticsDao")
	private FileStatisticsDAO fileStatisticsDao;
	
	//≤‚ ‘¥˙¬Î
	@RequestMapping("saveFileStatistics")
	public void saveFileStatistics(){
		String sql = " select * from resourcefile";
		@SuppressWarnings("unchecked")
		List<RESOURCEFILE> rl = (List<RESOURCEFILE>) resourceFileDao.findByQuery(sql, RESOURCEFILE.class);
		List<FILESTATISTICS> fsl = new LinkedList<FILESTATISTICS>();
		for(RESOURCEFILE file: rl){
			FILESTATISTICS fs = new FILESTATISTICS();
			fs.setContain_file_flag("no");
			fs.setCreate_time(System.currentTimeMillis());
			fs.setResourcefile_id(file.getId());
			fs.setSecret_num(0);
			fs.setSecret_result("wait");
			String sql1 = "select count(*) from context where file_id='" + file.getId() + "'";
			int t = (int) contextDao.findByQueryForList(sql1, Integer.class).get(0);
			fs.setTotal_num(t);
			fsl.add(fs);
		}
		fileStatisticsDao.batchSave(fsl);
	}
	
	@RequestMapping("saveResult")
	public void saveSiteResult(String taskid, String validFlag, HttpServletResponse response){
		//taskid = "a1c41245-8c93-4a58-8e1f-41ef51087a56";
		//validFlag = "valid";
		resultService.saveResult(validFlag, taskid);
		response.setContentType("text;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			String result =  "ok";
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
