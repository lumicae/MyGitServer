$(function(){
	$(".navDiv a:eq(0)").addClass("clk");
	loadTask();
	$(".navDiv a").each(function(index,ev){
		$(ev).click(function(){
			$(".navDiv a").removeClass("clk");
			$(ev).addClass("clk");
			console.log(index);
		})
	});
	adaptBodySize();
	$(window).resize(function(){
		adaptBodySize();
	});
});

function getPageListByTaskId(obj,curPage){
	var li = $(obj).parents("li");
	var taskId = $(li).find(".taskId").val();
	var taskType = $("input[name=taskType]").val();
	$("input[name=turnPageFlag]").val("taskId");
	//获取所有关键字列表，并将已检查的关键字设为选中状态
	var url = "/SecureInspect/preview/getPageListByTaskId";
	$.ajax({
		cache:false,
		sync: false,
		type:"post",
		data: {"taskId":taskId,"curPage":curPage,"taskType":taskType},
		url:url,
		dataType:"html",
		success:function(result){
			$(".navDiv a").removeClass("clk");
			$(".navDiv a:eq(1)").addClass("clk");
			$(".taskDiv").hide();
			$(".fileDiv").hide();
			$(".pageDiv").show();
			renderGUI(result);
		},
		error:function(e){
			console.log(e);
		}
	});
}

function getFileListByPageId(obj, curPage){
	var li = $(obj).parents("li");
	var pageId = $(li).find(".pageId").val();
	var taskType = $("input[name=taskType]").val();
	$("input[name=turnPageFlag]").val("pageId");
	//获取所有关键字列表，并将已检查的关键字设为选中状态
	var url = "/SecureInspect/preview/getFileListByPageId";
	$.ajax({
		cache:false,
		sync: false,
		type:"post",
		data: {"pageId":pageId, "curPage" : curPage, "taskType":taskType},
		url:url,
		dataType:"html",
		success:function(result){
			$(".navDiv a").removeClass("clk");
			$(".navDiv a:eq(2)").addClass("clk");
			$(".taskDiv").hide();
			$(".pageDiv").hide();
			$(".fileDiv").show();
			renderGUI(result);
		},
		error:function(e){
			console.log(e);
		}
	});
}

function loadTask(){
	$(".navDiv a").removeClass("clk");
	$(".navDiv a[name=task]").addClass("clk");
	$("input[name=turnPageFlag]").val("taskDefault");
	$("#searchForm input[name=curPage]").val("1");
	$("#searchForm input[name=pageSize]").val("10");
	 $.ajax({
		async : false,
		cache : false,
		type : "POST",
		url : "/SecureInspect/preview/firstLoadTask",
		data: $("#searchForm").serialize(),
		dataType : "html",
		success : function(result) {
			$(".taskDiv").show();
			$(".fileDiv").hide();
			$(".pageDiv").hide();
			renderGUI(result);
		},				
		error : function(msg) {
			console.log(msg);
		}
	}); 
}
function loadPage(){
	$(".navDiv a").removeClass("clk");
	$(".navDiv a[name=page]").addClass("clk");
	$("input[name=turnPageFlag]").val("pageDefault");
	$("#searchForm input[name=curPage]").val("1");
	$("#searchForm input[name=pageSize]").val("10");
	$.ajax({
		async : false,
		cache : false,
		type : "POST",
		url : "/SecureInspect/preview/firstLoadPage",
		data: $("#searchForm").serialize(),
		dataType : "html",
		success : function(result) {
			$(".taskDiv").hide();
			$(".fileDiv").hide();
			$(".pageDiv").show();
			renderGUI(result);
		},				
		error : function(msg) {
			console.log(msg);
		}
	});
}
function loadFile(){
	$(".navDiv a").removeClass("clk");
	$(".navDiv a[name=file]").addClass("clk");
	$("input[name=turnPageFlag]").val("fileDefault");
	$("#searchForm input[name=curPage]").val("1");
	$("#searchForm input[name=pageSize]").val("10");
	$.ajax({
		async : false,
		cache : false,
		type : "POST",
		url : "/SecureInspect/preview/firstLoadFile",
		data: $("#searchForm").serialize(),
		dataType : "html",
		success : function(result) {
			$(".taskDiv").hide();
			$(".pageDiv").hide();
			$(".fileDiv").show();
			renderGUI(result);
		},				
		error : function(msg) {
			console.log(msg);
		}
	});
}

function setBriefSearchPara(){
	var pageSize = $("select[name=pageSize]").val();
	$("input[name=pageSize]").val(pageSize);
	
	var temp = $(".searchInput").val();
	if($(".searchSelect").val() == "task_name"){
		$("input[name=briefSrchKey]").val("task_name");
		$("input[name=briefSrchVal]").val(temp);
	}
	else if($(".searchSelect").val() == "keyword"){
		$("input[name=briefSrchKey]").val("关键字");
		$("input[name=briefSrchVal]").val(temp);
	}
	var showType = $(".navDiv .clk").attr("name");
	$("input[name=turnPageFlag]").val(showType + "Brief");
}

function setAdvSearchPara(){
	var temp = $(".searchInput").val();
	if($(".searchSelect").val() == "task_name"){
		$("input[name=briefSrchKey]").val("task_name");
		$("input[name=briefSrchVal]").val(temp);
	}
	else if($(".searchSelect").val() == "keyword"){
		$("input[name=briefSrchKey]").val("关键字");
		$("input[name=briefSrchVal]").val(temp);
	}
	
	var pageSize = $("select[name=pageSize]").val();
	$("input[name=pageSize]").val(pageSize);
	var st = $("#startTime").val();
	var et = $("#endTime").val();
	if(st != ""){
		$("input[name=startTime]").val(st);
	}
	else{
		$("input[name=startTime]").val(0);
	}
	if(et != ""){
		$("input[name=endTime]").val(et);
	}
	else{
		$("input[name=endTime]").val(0);
	}
	$("input[name=location]").val($(".site").val());
	$("input[name=fileName]").val($(".fileName").val());
	$("input[name=secCondition]").val($(".secCondition").val());
	var showType = $(".navDiv .clk").attr("name");
	$("input[name=turnPageFlag]").val(showType + "Adv");
}

function briefSearch(){
	var showType = $(".navDiv .clk").attr("name");
	if($(".searchInput").val() == ""){
		switch(showType){
		case "task":loadTask();break;
		case "page":loadPage(); break;
		case "file":loadFile();break;
		}
		return;
	}
	setBriefSearchPara();
	var url = "/SecureInspect/preview/briefSearch" + showType;
	$("input[name=curPage]").val("1");
	var data = $("#searchForm").serialize();
	_AjaxRequest(url,data);
}

function advSearch(){
	$("input[name=curPage]").val("1");
	setAdvSearchPara();
	var showType = $(".navDiv .clk").attr("name");
	var url = "/SecureInspect/preview/advSearch" + showType;
	var data = $("#searchForm").serialize();
	_AjaxRequest(url,data);
}

function getDataTurnPage(curPage){
	var url = "";
	var data = null;
	var turnPageFlag = $("input[name=turnPageFlag]").val();
	var taskType = $("input[name=taskType]").val();
	$("#searchForm input[name=curPage]").val(curPage);
	var showType = $(".navDiv .clk").attr("name");
	switch(turnPageFlag){
	case "taskId" : url = "/SecureInspect/preview/getPageListByTaskId"; var taskId = $("." + showType + "Div input[name=taskId]").val();data={"taskId": taskId, "curPage" : curPage, "taskType" : taskType};
		break;
	case "pageId" : url = "/SecureInspect/preview/getFileListByPageId"; var pageId = $("." + showType + "Div input[name=pageId]").val();data={"pageId": pageId, "curPage" : curPage, "taskType": taskType};
		break;
	case "taskDefault" :
	case "taskBrief" : url = "/SecureInspect/preview/briefSearchtask"; data = $("#searchForm").serialize();
		break;
	case "pageDefault" :
	case "pageBrief" : url = "/SecureInspect/preview/briefSearchpage"; data = $("#searchForm").serialize();
		break;
	case "fileDefault" :
	case "fileBrief" : url = "/SecureInspect/preview/briefSearchfile"; data = $("#searchForm").serialize();
		break;
	case "taskAdv" : url = "/SecureInspect/preview/advSearchtask"; data = $("#searchForm").serialize();
		break;
	case "pageAdv" : url = "/SecureInspect/preview/advSearchpage"; data = $("#searchForm").serialize();
		break;
	case "fileAdv" : url = "/SecureInspect/preview/advSearchfile"; data = $("#searchForm").serialize();
		break;
	}
	_AjaxRequest(url, data);
}
function _AjaxRequest(url, data){
	$.ajax({
		type : "POST",
		async : false,
		cache : false,
		data : data,
		url : url,
		dataType : "html",
		success : function(result) {
			renderGUI(result);
		},
		error : function(error) {
			console.log(error);
		}
	});
}


function renderGUI(result){
	var showType = $(".navDiv .clk").attr("name");
	$("." + showType + "Div").html(result);
	var totalPageNum = $("." + showType + "Div input[name=totalPageNum]").val();
	var curPage = $("." + showType + "Div input[name=curPage]").val();
	var wrap = $("." + showType + "Div")[0];
	setPage(curPage, totalPageNum, 10, wrap);
	showRedKeyInPage($("." + showType + "Div"));
	setTypeShowStyle($("." + showType + "Div"));
}
/**
 * 设置网页中是否包含文件时的正文类型的显示方式，
 * 若不包含文件，则类型区域只显示正文；
 * 若包含文件，则类型区域还要包含查看文件的链接。
 */
function setTypeShowStyle(obj){
	var liList = $(obj).find("ul li.licon");
	
	for(var i=0; i<liList.size();i++){
		var lia = liList[i];
		var height = $(lia).height();
		var type = $(lia).find(".type input").val();
		if(type == "yes"){
			$(lia).find(".type span").height(height/2);
			$(lia).find(".type span").css({"line-height": height/2 + "px"});
			$(lia).find(".operation span").height(height/2);
			$(lia).find(".operation span input").css({"margin-top":(height/2-25)/2 + "px"});
		}
		else{
			$(lia).find(".type span").height(height);
			$(lia).find(".type span").css({"line-height": height + "px"});
			$(lia).find(".operation span").height(height);
			$(lia).find(".operation span input").css({"margin-top":(height-25)/2 + "px"});
		}
	}
}

//将正文中的敏感关键字标红
function showRedKeyInPage(obj){
	var liList = $(obj).find("ul li.licon");
	
	for(var i=0; i<liList.size();i++){
		var lia = liList[i];
		var keyArr = $(lia).find(".context input[name=key]");
		var contextArr = $(lia).find(".context .contextSpan");
		
		for(var j=0; j<keyArr.size(); j++){
			var key = $(keyArr[j]).val();
			var context = $(contextArr[j]).html();
			var str = "/" + key +"/g";
			var reg = eval(str);
			var regAfter = "<span style='color:red;vertical-align:baseline;'>" + key + "</span>";
			var newContext = context.replace(reg,regAfter);
			$(contextArr[j]).html(newContext);
		}
	}
}
/**
 * 设置展示区大小为自适应模式
 */
function adaptBodySize(){
	var window_height = $(window).height() - 20;
	$(".rightWrap").height(window_height);
}

function getTime(){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
}
/**
 * 处理上下文
 * @param obj
 */
function dealContext(obj){
	var li = $(obj).parents("li");
	var pageId = $(li).find(".pageId").val();
	var file_url = $(li).find(".URL").html();
	var file_name = $(li).find(".title").html();

	var url = "/SecureInspect/preview/dealContext";
	var height = $(window.parent.document.body).height();
	var width = $(window.parent.document.body).width();
	$.ajax({
		cache:false,
		sync: false,
		type:"get",
 		data:{"pageId":pageId, "file_url": file_url, "file_name": file_name},
		url:url,
		dataType:"html",
		success:function(data){
			var html = "<div class='mask' style='width:" + width + "px;height:" + height + "px; top:0px; left:0px; background:#000; opacity:0.6;position:absolute;'>" + 
			"<a class='close' onclick='close(this)' style='background:url(/SecureInspect/images/close.png) 1px -2px no-repeat; cursor:pointer;position:absolute; width:59px;height:55px; top:4px;right:0;'></a>" +
			"</div>"+data;
			$(window.parent.document.body).append(html);
			showRedKeyInDealContext($(window.parent.document.body).find(".back"));
			bindCloseHv();
			bindCloseClk();
			bindSaveClk(obj);
		},
		error:function(e){
			console.log(e);
		}
	});
}

function showRedKeyInDealContext(obj){
	var liList = $(obj).find("ul li");
	for(var i=1; i<liList.size();i++){
		var li = liList.get(i);
		var key = $(li).find(".key").html();
		var context = $(li).find(".contextSpan").html();
		var str = "/" + key +"/g";
		var reg = eval(str);
		var regAfter = "<span style='color:red;vertical-align:baseline;'>" + key + "</span>";
		var newContext = context.replace(reg,regAfter);
		$(li).find(".contextSpan").html(newContext);
	}
}

function bindCloseClk(){
	$(window.parent.document.body).find(".close").on("click", function(){
		var obj = $(this);
		clearMask();
	})
}
function bindSaveClk(obj){
	$(window.parent.document.body).find(".save").on("click", function(){
		var back = $(window.parent.document.body).find(".back");
		var file_id = $(back).find("input[name=fileId]").val();
		var url = "/SecureInspect/preview/saveContextResult";
		var liArr = $(back).find("li");
		var contextResultArr=[];
		var tip = "";
		
		for(var i=1; i<liArr.size(); i++){
			var li = liArr[i];
			var contextResult ={};
			contextResult.id = $(li).find("input[name=context_id]").val();
			contextResult.result = $(li).find("input[class=result]:checked").val();
			if($(li).find("input[class=result]").val() == null || $(li).find("input[class=result]").val()==""){
				tip="未完成全部结果判定!";
			break;
			}
			if(tip!=""){
				alert(tip);
				return;
			}
			contextResultArr.push(contextResult);
		}
		$.ajax({
			cache:false,
			sync:false,
			type:"get",
			data:{"file_id": file_id, "contextResultList" : JSON.stringify(contextResultArr)},
			url:url,
			dataType:"text",
			success:function(flag){
				if(flag=="ok"){
					alert("保存成功！");
					var li = $(obj).parents("li");
					var pageId = $(li).find(".pageId").val();
					var urlp = "/SecureInspect/preview/getPageSecureResult";
					$.ajax({
						cache:false,
						sync:false,
						type:"get",
						data:{"pageId": pageId},
						url:urlp,
						dataType:"json",
						success:function(data){
							var re = "";
							
							if(data.secret_result == "secret"){
								re = "涉密";
							}
							else if(data.secret_result == "no_secret"){
								re = "非涉密";
							}
							else if(data.secret_result == "suspect"){
								re = "可疑";
							}
							$(li).find(".secureResult").html(re);
							$(li).find(".secureCnt").html(data.secret_num);
						},
						error:function(msg){
							console.log(msg);
						}
					});
				}
				else{
					alert("保存失败！")
				}
			},
			error:function(e){
				console.log(e);
			}
		});
	})
}
function clearMask(){
	$(window.parent.document.body).find(".mask").remove();
	$(window.parent.document.body).find(".back").remove();
}
function bindCloseHv(){
	$(window.parent.document.body).find(".mask .close").on("mouseover mouseout",function(event){
		var obj = $(this);
		 if(event.type == "mouseover"){
			 $(obj).css({"background": "url('/SecureInspect/images/close.png') -97px 0px no-repeat"});
		 }else if(event.type == "mouseout"){
			 $(obj).css({"background": "url('/SecureInspect/images/close.png') 1px -2px no-repeat"});
		 }
	})
}


/* <!-- <iframe name="dIframe" style="display:none"></iframe>
以form方式提交网站信息的原因：1.便于接收返回的doc文档；2.将网站信息放在input中便于提交
<form id="downLoad" style="display:none" target="dIframe">
	<input name="siteInfo" id="siteInfo" type="hidden">
	<input name="siteId" id="siteId" type=hidden">
	<input name="siteName" id="siteName" type="hidden">
</form> --> */
//分别导出一至多个网站的检查结果报告
function exportReport(){
	var itemList = document.getElementsByName("itemCk");
	
	for(var i=0; i<itemList.length; i++){
		if (itemList[i].checked){
			setTimeout(downiDoc(itemList[i],i), 2000);
		}
	}
}

function downiDoc(item, i){
	var url = "/SecureInspect/preview/genReport";
	var downDiv = document.getElementsByClassName("downDoc");
	
	var iframe = document.createElement("iframe");
	iframe.name = "dIframe" + i;
	iframe.setAttribute("style","display:none");
	downDiv[0].appendChild(iframe);
	
	var form = document.createElement("form");
	form.setAttribute("style", "display:none");
	form.setAttribute("target", "dIframe" + i);
	form.name= "dForm" + i;
	var input1 = document.createElement("input");
	input1.name="taskId";
	input1.type="hidden";
	input1.value=$(item).parents("li").find(".taskId").val();
	var input2 = document.createElement("input");
	input2.name="taskName";
	input2.type="hidden";
	input2.value=$(item).parents("li").find(".name").html()
	form.appendChild(input1);
	form.appendChild(input2);
	downDiv[0].appendChild(form);
	form.action = url;
	form.submit();
}

//批量导出一至多个网站的检查结果报告
function batchExportReport(){
	var itemList = document.getElementsByName("itemCk");
	var url = "/SecureInspect/preview/sendObjectArr";
	var siteArr = [];
	for(var i=0; i<itemList.length; i++){
		var site = {};
		if (itemList[i].checked){
			site.id = $(itemList[i]).parents("li").find(".taskId").val();
			site.name = $(itemList[i]).parents("li").find(".name").html();
			siteArr.push(site);
		}
	}
	var str = JSON.stringify(siteArr);
	var formD = $("#downLoad")[0];
	formD.action = url;
	$("#taskInfo").val(str);
	formD.submit();
}

function checkAll(){
	var type = $(".navDiv .clk").attr("name");
	var flag = $("." + type + "Div #batch").is(":checked");
	if(flag){
		$("." + type + "Div #batch").next("label").html("全不选");
	}
	else{
		$("." + type + "Div #batch").next("label").html("全选");
	}
	$("." + type + "Div .itemCk").attr('checked', flag);
}

function regUrl(url){
	var patt1=/(http|ftp|https)?:\/\/[\w]{1,6}(\.[\w]{1,20}){1,3}\/?/;
	var patt2 = /[\w]{1,6}(\.[\w]{1,20}){1,3}/;
	if(patt1.test(url)){
		var t = new Array();
		t = url.match(patt2);
		console.log(t);
		return t[0];
	}
	else{
		alert("URL格式不正确");
		return false;
	}
}
