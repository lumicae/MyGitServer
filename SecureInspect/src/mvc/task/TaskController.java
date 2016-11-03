package mvc.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import logic.task.TaskService;
import mvc.account.TaskPara;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import redis.RedisAPI;
import utils.ChineseToSpellUtil;
import utils.PageView;
import utils.PathUtil;
import db.table.keyTask.bean.KEYTASK;
import db.table.keyTask.dao.KeyTaskDao;
import db.table.keyword.bean.KEYWORD;
import db.table.keyword.dao.KeywordDAO;
import db.table.task.bean.TASK;
import db.table.task.dao.TaskDAO;

@Controller
public class TaskController {
	
	@Autowired
	@Qualifier("taskService")
	private TaskService taskService;
	
	@Autowired
	@Qualifier("taskDao")
	private TaskDAO taskDao;
	
	@Autowired
	@Qualifier("keywordDao")
	private KeywordDAO keywordDao;
	
	@Autowired
	@Qualifier("keyTaskDao")
	private KeyTaskDao keyTaskDao;
	
	@RequestMapping("/taskHome")
	public ModelAndView taskHome(){
		ModelAndView mav = new ModelAndView();
		List<KEYWORD> keywordList = new LinkedList<KEYWORD>();
		taskService.getKeywordList(keywordList);
		mav.addObject("keywordList", keywordList);
		mav.setViewName("taskHome");
		return mav;
	}
	
	@RequestMapping("/loadTask/{taskType}")
	public ModelAndView task(@PathVariable("taskType") String taskType){
		ModelAndView mav = new ModelAndView();
		
		PageView<TaskPara> pageview = new PageView<TaskPara>(10);
		taskService.findTaskInfo(pageview, 1, taskType);
		mav.addObject("pageview", pageview);
		mav.addObject("taskType", taskType);
		mav.setViewName("task0");
		return mav;
	}
	
	@RequestMapping("addTask")
	public void addTask(@ModelAttribute("TaskRecPara")TaskRecPara taskRecPara, HttpServletResponse response){
		String siteurl = taskRecPara.getSiteurl();
		String kwds = taskRecPara.getKwds();
		String type = taskRecPara.getType();
		String sitename = taskRecPara.getSitename();
		
		String msg = "";
	
		//����������
		String taskId = taskService.saveScanTask(type, kwds, siteurl, sitename);
		
		String dirName = ChineseToSpellUtil.getSpell(sitename, false);
		TaskInfoPara taskInfoPara = new TaskInfoPara();
		taskInfoPara.setDirname(dirName);
		taskInfoPara.setKwds(kwds);
		taskInfoPara.setSitename(sitename);
		taskInfoPara.setSiteurl(siteurl);
		taskInfoPara.setType( type + "check");
		taskInfoPara.setTaskid(taskId);
	
		//����֪ͨ
		JSONObject jo = JSONObject.fromObject(taskInfoPara);
		String obj = jo.toString();
		
		System.out.println("New Task:" + obj);
		
		try{
			RedisAPI ra = new RedisAPI();
			ra.set(obj);
		}
		catch(Exception e){
			
			System.out.println("add Task: " + obj + " failed");
			e.printStackTrace();
		}
		System.out.println("add Task: " + obj + " finished");
		//����
		msg = "ok";
		String command = "window.parent.uploadFileCallback('" + msg + "');";
		command = "<script type='text/javascript'>" + command + "</script>";
		try {
			response.getWriter().print(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * �½��������
	 */
	@RequestMapping("addEmailTask")
	public void addEmailTask(@RequestParam("file") MultipartFile file, @ModelAttribute("TaskRecPara")TaskRecPara taskRecPara, HttpServletResponse response){
		
		String kwds = taskRecPara.getKwds();
		String type = taskRecPara.getType();
		String sitename = taskRecPara.getSitename();
		System.out.println(file.getName());
		System.out.println(file.getOriginalFilename() + " " + file.getSize());
		String msg = "";
		String fileType = "";
		if(type.equals("email")){
			if(file.getSize() == 0){
				msg = "���ϴ��ļ�";
			}
			fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
			System.out.println(fileType);
			if(!fileType.equals("zip") && !fileType.equals("rar")){
				msg = "�ļ���ʽ����ȷ";
			}
		}
		//����������
		String taskId = taskService.saveScanTask(type, kwds, file.getOriginalFilename(), sitename);
		String siteurl = PathUtil.mail_dir + taskId;
		String dirName = ChineseToSpellUtil.getSpell(sitename, false);
		TaskInfoPara taskInfoPara = new TaskInfoPara();
		taskInfoPara.setDirname(dirName);
		taskInfoPara.setKwds(kwds);
		taskInfoPara.setSitename(sitename);
		taskInfoPara.setSiteurl(siteurl);
		taskInfoPara.setType( type + "check");
		taskInfoPara.setTaskid(taskId);
	
		//����֪ͨ
		JSONObject jo = JSONObject.fromObject(taskInfoPara);
		String obj = jo.toString();
		
		System.out.println("New Task:" + obj);
		
		try{
			RedisAPI ra = new RedisAPI();
			ra.set(obj);
		}
		catch(Exception e){
			
			System.out.println("add Task: " + obj + " failed");
			e.printStackTrace();
		}
		System.out.println("add Task: " + obj + " finished");
		//����
		msg = "ok";
		String command = "window.parent.uploadFileCallback('" + msg + "');";
		command = "<script type='text/javascript'>" + command + "</script>";
		try {
			response.getWriter().print(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//�����email�����ѹzip�ļ�
		if(type.equals("email")){
			//����zip�ļ�
			String zipPath = PathUtil.temp_dir + taskId + "." + fileType;
			File dir1 = new File(PathUtil.temp_dir);
			if(!dir1.exists()){
				dir1.mkdirs();
			}
			InputStream is;
			try {
				is = file.getInputStream();
				FileOutputStream fos = new FileOutputStream(zipPath);
				byte[] b = new byte[1024];
				while((is.read(b)) != -1){
				fos.write(b);
				}
				is.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//��ѹzip�ļ�
			String emailPath = PathUtil.mail_dir + taskId;
			File dir2 = new File(emailPath);
			 if(!dir2.exists()) dir2.mkdirs();
			 try {
				 Process proc = Runtime.getRuntime().exec("cmd /c \"D:/Program Files/WinRAR/WinRAR.exe\" x -o+ " + zipPath + " " + emailPath);
				 proc.waitFor();
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 //��ѹ��ɺ�ɾ��.zip�ļ�
			 File zipFile = new File(zipPath);
			 if(zipFile.exists()) zipFile.delete();
		}
	
	}
	
	@RequestMapping("taskProcess")
	public void  taskProcess(String obj, HttpServletResponse response){
		String result = "";
		try{
			RedisAPI re = new RedisAPI();
			result = re.get(obj);
		}
		catch (Exception e){
			System.out.println("��ȡ" + obj + "�����쳣");
			e.printStackTrace();
		}
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			String json =  result;
			pw.write(json);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("updateStatus")
	public void updateStatus(String taskid, HttpServletResponse response){
		
		taskService.updateTaskStatus(taskid);
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			String json =  "ok";
			pw.write(json);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/saveKeyTask")
	public void saveKeyTask(){
		String sql = "select * from task where type = 'site'";
		String sql1 = "select * from keyword";
		List<TASK> taskList = (List<TASK>) taskDao.findByQuery(sql, TASK.class);
		List<KEYWORD> keywordList = (List<KEYWORD>) keywordDao.findByQuery(sql1, KEYWORD.class);
		List<KEYTASK> keyTaskList = new LinkedList<KEYTASK>();
		for(TASK task : taskList){
			String taskId = task.getId();
			for(KEYWORD keyword : keywordList){
				KEYTASK keyTask = new KEYTASK();
				keyTask.setCreate_time(System.currentTimeMillis());
				keyTask.setKeyword_id(keyword.getId());
				keyTask.setTask_id(taskId);
				keyTaskList.add(keyTask);
			}
		}
	keyTaskDao.batchSave(keyTaskList);
	}
	
	@RequestMapping("/restartTask")
	public void restartTask(){
		String sql = "select * from task where status = 'δ��ʼ' and type='site'";
		@SuppressWarnings("unchecked")
		List<TASK> taskList = (List<TASK>) taskDao.findByQuery(sql, TASK.class);
		for(TASK task : taskList){
			TaskInfoPara taskInfoPara = new TaskInfoPara();
			String dirName = ChineseToSpellUtil.getSpell(task.getName(), false);
			taskInfoPara.setDirname(dirName);
			taskInfoPara.setKwds(keywordDao.getKwdsByTaskId(task.getId()));
			taskInfoPara.setSitename(task.getName());
			taskInfoPara.setSiteurl(task.getLocation());
			taskInfoPara.setType( task.getType() + "check");
			JSONObject jo = JSONObject.fromObject(taskInfoPara);
			String obj = jo.toString();
			System.out.println("New Task:" + obj);
			try{
				RedisAPI ra = new RedisAPI();
				ra.set(obj);
			}
			catch(Exception e){
				
				System.out.println("add Task: " + obj + " failed");
				e.printStackTrace();
			}
			System.out.println("add Task: " + obj + " finished");
		}
	}
}
