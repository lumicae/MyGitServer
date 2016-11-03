package mvc.site;

import java.util.LinkedList;
import java.util.List;

public class FilePrePara {
	
	private String fileId;
	
	private String title;
	
	private String URL;
	
	private String src;
	
	private List<KeyContext> keyContextList= new LinkedList<KeyContext>();
	
	private String type;
	
	private String iresult;
	
	private int secureCnt;
	
	private String containFileFlag;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public List<KeyContext> getKeyContextList() {
		return keyContextList;
	}

	public void setKeyContextList(List<KeyContext> keyContextList) {
		this.keyContextList = keyContextList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getIresult() {
		return iresult;
	}

	public void setIresult(String iresult) {
		this.iresult = iresult;
	}

	public int getSecureCnt() {
		return secureCnt;
	}

	public void setSecureCnt(int secureCnt) {
		this.secureCnt = secureCnt;
	}

	public String getContainFileFlag() {
		return containFileFlag;
	}

	public void setContainFileFlag(String containFileFlag) {
		this.containFileFlag = containFileFlag;
	}
}
