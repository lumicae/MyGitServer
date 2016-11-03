/**
 * 分页
 */
function setPage(curPageNum, totalPageNum, showPageNum, wrap){
	if(totalPageNum == null || totalPageNum == "") return;
	var container = document.createElement("div");
	container.id="pageDiv";
	$(wrap).append(container);
	
	var curPage = parseInt(curPageNum);
	var totalPage = parseInt(totalPageNum);
	var showPage = parseInt(showPageNum);
	if(totalPage < showPage){
		showPage = totalPage;
	}
	var pageDiv = container;
	var lrPage = Math.floor(showPage/2);
	if(curPage < 1){
		console.log("当前输入页码错误");
		return;
	}
	if(curPage > 1){
		var oA = document.createElement('a');
		oA.href = '#1';
		oA.innerHTML = '首页';
		pageDiv.appendChild(oA);
	}
	if(curPage > 1){
		var oA = document.createElement('a');
		oA.href='#' + (curPage-1);
		oA.innerHTML = '上一页';
		pageDiv.appendChild(oA);
	}
	if(curPage < showPage || totalPage == showPage){
		for(var i=1; i<=showPage; i++){
			var oA = document.createElement('a');
			oA.href = '#' + i;
			if(curPage == i){
				oA.innerHTML = i;
			}
			else{
				oA.innerHTML = '[' + i + ']';
			}
			pageDiv.appendChild(oA);
		}
	}
	else{
		if(totalPage - curPage < lrPage && curPage == totalPage-1){//倒数第二页
			for(var i=1; i<=showPage; i++){
				console.log(curPage-showPage + i);
				var oA = document.createElement('a');
				oA.href = '#' + (curPage - (showPage-1) + i);
				if(curPage == (curPage - (showPage-1) + i)){
					oA.innerHTML = curPage - (showPage-1) + i;
				}
				else{
					oA.innerHTML = '[' + (curPage - (showPage-1) + i) + ']';
				}
				pageDiv.appendChild(oA);
			}
		}
		else if(totalPage - curPage < lrPage && curPage == totalPage){//最后一页
			for(var i=1; i<=showPage; i++){
				var oA = document.createElement('a');
				oA.href = '#' + (curPage - showPage +i);
				
				if(curPage == (curPage - showPage + i)){
					oA.innerHTML = curPage - showPage + i;
				}
				else{
					oA.innerHTML = '[' + (curPage - showPage + i) + ']';
				}
				pageDiv.appendChild(oA);
			}
		}
		else{
			for(var i=1; i<= showPage; i++){
				var oA = document.createElement('a');
				oA.href = '#' + (curPage - (showPage - lrPage) + i);
				if(curPage == (curPage - (showPage - lrPage) + i)){
					oA.innerHTML  = curPage - (showPage - lrPage) + i;
				}
				else{
					if(curPage - (showPage - lrPage) + i > totalPage){
						break;
					}
					oA.innerHTML  = '[' + (curPage - (showPage - lrPage) + i) + ']';
				}
				pageDiv.appendChild(oA);
			}
		}
	  }
	if(curPage < totalPage){
		for(var i=1; i<=2; i++){
			if(i==1){
				var oA = document.createElement('a');
				oA.href = '#' + (parseInt(curPage) + 1);
				oA.innerHTML = '下一页';
			}
			else{
				var oA = document.createElement('a');
				oA.href = '#' + totalPage;
				oA.innerHTML = '尾页';
			}
			pageDiv.appendChild(oA);
		}
	}
	var oA = $("#pageDiv a");
	for(var i=0; i<$(oA).length; i++){
		$(oA).eq(i)[0].onclick = function(){
			
			var clickPage = this.getAttribute('href').substring(1);
			//if(clickPage == curPageNum) return;
			pageDiv.innerHTML = '';
			getDataTurnPage(clickPage);
		}
	}
}