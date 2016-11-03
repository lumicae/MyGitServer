package mvc.site;

import java.util.LinkedList;
import java.util.List;

public class PagePrePara {
	
	private String pageId;

	private String title;
	
	private String URL;
	
	private String src;
	
	private List<KeyContext> keyContextList = new LinkedList<KeyContext>();
	
	private int secureCnt;
	
	private String iresult;
	
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

	public int getSecureCnt() {
		return secureCnt;
	}

	public void setSecureCnt(int secureCnt) {
		this.secureCnt = secureCnt;
	}

	public String getIresult() {
		return iresult;
	}

	public void setIresult(String iresult) {
		this.iresult = iresult;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getContainFileFlag() {
		return containFileFlag;
	}

	public void setContainFileFlag(String containFileFlag) {
		this.containFileFlag = containFileFlag;
	}
}
