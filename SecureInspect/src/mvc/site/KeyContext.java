package mvc.site;

public class KeyContext {
	
	private String context_id;
	
	private String key;
	
	private String context;
	
	//处理页面展示已有结果
	private String result;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext_id() {
		return context_id;
	}

	public void setContext_id(String id) {
		this.context_id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
