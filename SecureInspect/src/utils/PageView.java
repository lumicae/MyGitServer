package utils;

import java.util.List;

public class PageView<T> {
	
	//单页数据条目数
	private int pageSize = 10;

	//当前页
	private long curPage = 1;
	
	//数据条目总数
	private long totalNum = 0;
	
	//总页数
	private long totalPageNum = 1;
	
	//当前页中的数据条目
	private List<T> items;
	
	//默认构造空页
	public PageView(int pageSize){
		this.pageSize = pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public long getCurPage() {
		return curPage;
	}

	public boolean setCurPage(long curPage){
		if( 1 <= curPage && curPage <= this.totalPageNum){
			this.curPage = curPage;
			
			return true;
		}
		else
			return false;
	}
	
	public long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
		if( totalNum % pageSize == 0 ){
			if( totalNum != 0 ){
				setTotalPageNum( totalNum / pageSize );
			}
			else{
				setTotalPageNum( 1 );
			}
		}
		else{
			setTotalPageNum( totalNum / pageSize + 1);
		}
	}

	public long getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(long totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
	
	public long getStartNum(){
		long startNum = (this.curPage - 1) * this.pageSize + 1;
		return startNum;
	}
	
	public long getFinishNum(){
		long finishNum = this.curPage * this.pageSize;
		return finishNum;
	}
	
	public long getLimit1sdPara(){
		long startNum = (this.curPage - 1) * this.pageSize;
		return startNum;
	}
	
	//获取limit语句的第二个参数，sql查询中返回的数据条目数
	public long getLimit2rdPara(){
		long para = 0l;
		if((curPage * pageSize - totalNum) >0){
			para = totalNum - (curPage-1) * pageSize;
		}
		else{
			para = pageSize;
		}
		
		return para;
	}
}
