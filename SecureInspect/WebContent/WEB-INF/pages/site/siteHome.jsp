<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="/SecureInspect/css/siteHome.css"/>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/SecureInspect/js/calendar/calendar.js"></script>
<script type="text/javascript" src="/SecureInspect/js/pageTool.js"></script>
<script type="text/javascript">
$(function(){
	$(".navDiv a:eq(0)").addClass("clk");
	loadSite();
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

function dealContext(obj){
	var li = $(obj).parents("li");
	var pageId = $(li).find(".pageId").val();
	var file_url = $(li).find(".URL").html();
	var file_name = $(li).find(".name").html();

	var url = "/SecureInspect/site/dealContext";
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
		var url = "/SecureInspect/site/saveContextResult";
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
					var urlp = "/SecureInspect/site/getPageSecureResult";
					$.ajax({
						cache:false,
						sync:false,
						type:"get",
						data:{"pageId": pageId},
						url:urlp,
						dataType:"text",
						success:function(result){
							var re = "";
							if(result == "secret"){
								re = "涉密";
							}
							else if(result == "no_secret"){
								re = "非涉密";
							}
							else if(result == "suspect"){
								re = "可疑";
							}
							$(li).find(".secureResult").html(re);
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
function getPageList(obj, curPage){
	var li = $(obj).parents("li");
	var taskId = $(li).find(".taskId").val();
	
	//获取所有关键字列表，并将已检查的关键字设为选中状态
	var url = "/SecureInspect/site/getPageListBySiteId";
	$.ajax({
		cache:false,
		sync: false,
		type:"post",
		data: {"taskId":taskId, "curPage": curPage},
		url:url,
		dataType:"html",
		success:function(data){
			$(".navDiv a").removeClass("clk");
			$(".navDiv a:eq(1)").addClass("clk");
			$(".siteDiv").hide();
			$(".fileDiv").hide();
			$(".pageDiv").show();
			$(".pageDiv").html(data);
			$(".pageDiv input[name=byPageFlag]").val("taskId");
			var pageSize = 10;
			var totalPageNum = $(".pageDiv input[name=totalPageNum]").val();
			var curPage = $(".pageDiv input[name=curPage1]").val();
			var wrap = $(".pageDiv")[0];
			setPage(curPage, totalPageNum, 10, wrap);
			showRedKeyInPage($(".pageDiv"));
			setTypeShowStyle($(".pageDiv"));
		},
		error:function(e){
			console.log(e);
		}
	});
}

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
function getFileList(obj, curPage){
	var li = $(obj).parents("li");
	var pageId = $(li).find(".pageId").val();
	
	//获取所有关键字列表，并将已检查的关键字设为选中状态
	var url = "/SecureInspect/site/getFileListByPageId";
	$.ajax({
		cache:false,
		sync: false,
		type:"post",
		data: {"pageId":pageId, "curPage" : curPage},
		url:url,
		dataType:"html",
		success:function(data){
			$(".navDiv a").removeClass("clk");
			$(".navDiv a:eq(2)").addClass("clk");
			$(".siteDiv").hide();
			$(".pageDiv").hide();
			$(".fileDiv").show();
			$(".fileDiv").html(data);
			$(".fileDiv input[name=byPageFlag]").val("pageId");
			var pageSize = 10;
			var totalPageNum = $(".fileDiv input[name=totalPageNum]").val();
			var curPage = $(".fileDiv input[name=curPage1]").val();
			var wrap = $(".fileDiv")[0];
			setPage(curPage, totalPageNum, 10, wrap);
			showRedKeyInPage($(".fileDiv"));
			setTypeShowStyle($(".fileDiv"));
		},
		error:function(e){
			console.log(e);
		}
	});
}
function adaptBodySize(){
	var window_height = $(window).height() - 20;
	$(".rightWrap").height(window_height);
}

function getTime(){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
}
function loadSite(){
	 $.ajax({
		async : false,
		cache : false,
		type : "POST",
		url : "/SecureInspect/site/firstLoadSite",
		dataType : "html",
		success : function(data) {
			$(".siteDiv").show();
			$(".siteDiv").html(data);
			$(".fileDiv").hide();
			$(".pageDiv").hide();
			$(".siteDiv input[name=byPageFlag]").val("sitedefault");
			var pageSize = 10;
			var totalPageNum = $(".siteDiv input[name=totalPageNum]").val();
			var curPage = $(".siteDiv input[name=curPage1]").val();
			var wrap = $(".siteDiv")[0];
			setPage(curPage, totalPageNum, 10, wrap);
		},				
		error : function(msg) {
			console.log(msg);
		}
	}); 
}
function loadPage(){
	$.ajax({
		async : false,
		cache : false,
		type : "POST",
		url : "/SecureInspect/site/firstLoadPage",
		dataType : "html",
		success : function(data) {
			$(".siteDiv").hide();
			$(".fileDiv").hide();
			$(".pageDiv").show();
			$(".pageDiv").html(data);
			$(".pageDiv input[name=byPageFlag]").val("pagedefault");
			var pageSize = 10;
			var totalPageNum = $(".pageDiv input[name=totalPageNum]").val();
			var curPage = $(".pageDiv input[name=curPage1]").val();
			var wrap = $(".pageDiv")[0];
			setPage(curPage, totalPageNum, 10, wrap);
			showRedKeyInPage($(".pageDiv"));
			setTypeShowStyle($(".pageDiv"));
		},				
		error : function(msg) {
			console.log(msg);
		}
	});
}
function loadFile(){
	$.ajax({
		async : false,
		cache : false,
		type : "POST",
		url : "/SecureInspect/site/firstLoadFile",
		dataType : "html",
		success : function(data) {
			$(".siteDiv").hide();
			$(".pageDiv").hide();
			$(".fileDiv").show();
			$(".fileDiv").html(data);
			$(".pageDiv input[name=byPageFlag]").val("filedefault");
			var pageSize = 10;
			var totalPageNum = $(".fileDiv input[name=totalPageNum]").val();
			var curPage = $(".fileDiv input[name=curPage1]").val();
			var wrap = $(".fileDiv")[0];
			setPage(curPage, totalPageNum, 10, wrap);
			showRedKeyInPage($(".fileDiv"));
			setTypeShowStyle($(".fileDiv"));
		},				
		error : function(msg) {
			console.log(msg);
		}
	});
}

function briefSearch(){
	var type = $(".navDiv .clk").attr("name");
	if($(".searchInput").val() == ""){
		switch(type){
		case "site":loadSite();break;
		case "page":loadPage(); break;
		case "file":loadFile();break;
		}
		return;
	}
	var pageSize = $("select[name=pageSize]").val();
	$("input[name=type]").val(type);
	var temp = $(".searchInput").val();
	var url = "/SecureInspect/site/briefSearch" + type;
	$("input[name=pageSize]").val(pageSize);
	$("input[name=curPage]").val("1");
	console.log($(".searchSelect").val());
	if($(".searchSelect").val() == "task_name"){
		$("input[name=briefSrchKey]").val("task_name");
		$("input[name=briefSrchVal]").val(temp);
	}
	else if($(".searchSelect").val() == "keyword"){
		$("input[name=briefSrchKey]").val("关键字");
		$("input[name=briefSrchVal]").val(temp);
	}
	
	$.ajax({
		type : "POST",
		async : false,
		cache : false,
		data : $("#searchForm").serialize(),
		url : url,
		dataType : "html",
		success : function(result) {
			$("." + type + "Div").html(result);
			$("." + type + "Div input[name=byPageFlag]").val(type + "brief");
			var totalPageNum = $("." + type + "Div input[name=totalPageNum]").val();
			var curPage = $("." + type + "Div input[name=curPage1]").val();
			var wrap = $("." + type + "Div")[0];
			setPage(curPage, totalPageNum, 10, wrap);
			showRedKeyInPage($("." + type + "Div"));
			setTypeShowStyle($("." + type + "Div"));
		},
		error : function(error) {
			console.log(error);
		}
	});
}

function advSearch(){
	var type = $(".navDiv .clk").attr("name");
	
	var temp = $(".searchInput").val();
	
	var pageSize = $("select[name=pageSize]").val();
	var url = "/SecureInspect/site/advSearch" + type;
	if($(".searchSelect").val() == "task_name"){
		//var url = regUrl(temp);
		$("input[name=briefSrchKey]").val("task_name");
		$("input[name=briefSrchVal]").val(temp);
	}
	else if($(".searchSelect").val() == "keyword"){
		$("input[name=briefSrchKey]").val("关键字");
		$("input[name=briefSrchVal]").val(temp);
	}
	$("input[name=curPage]").val("1");
	$("input[name=type]").val(type);
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
	
	$.ajax({
		type : "POST",
		async : false,
		cache : false,
		data : $("#searchForm").serialize(),
		url : url,
		dataType : "html",
		success : function(result) {
			$("." + type + "Div").html(result);
			$("." + type + "Div input[name=byPageFlag]").val(type + "adv");
			var totalPageNum = $("." + type + "Div input[name=totalPageNum]").val();
			var curPage = $("." + type + "Div input[name=curPage1]").val();
			var wrap = $("." + type + "Div")[0];
			setPage(curPage, totalPageNum, 10, wrap);
			showRedKeyInPage($("." + type + "Div"));
			setTypeShowStyle($("." + type + "Div"));
		},
		error : function(error) {
			console.log(error);
		}
	});
}

function getDataByPage(curPage){
	$("input[name=curPage]").val(curPage);
	var url = "";
	var type = $(".navDiv .clk").attr("name");
	var pageSize= $("select[name=pageSize]").val();
	var byPageFlag = $("." + type + "Div input[name=byPageFlag]").val();
	var data = null;
	switch(byPageFlag){
	case "taskId" : url = "/SecureInspect/site/getPageListBySiteId"; var taskId = $("." + type + "Div input[name=taskId]").val();data={"taskId": taskId, "curPage" : curPage};
		break;
	case "pageId" : url = "/SecureInspect/site/getFileListByPageId"; var pageId = $("." + type + "Div input[name=pageId]").val();data={"pageId": pageId, "curPage" : curPage};
		break;
	case "sitedefault" :
	case "sitebrief" : url = "/SecureInspect/site/briefSearchsite"; data = $("#searchForm").serialize();
		break;
	case "pagedefault" :
	case "pagebrief" : url = "/SecureInspect/site/briefSearchpage"; data = $("#searchForm").serialize();
		break;
	case "filedefault" :
	case "filebrief" : url = "/SecureInspect/site/briefSearchfile"; data = $("#searchForm").serialize();
		break;
	case "siteadv" : url = "/SecureInspect/site/advSearchsite"; data = $("#searchForm").serialize();
		break;
	case "pageadv" : url = "/SecureInspect/site/advSearchpage"; data = $("#searchForm").serialize();
		break;
	case "fileadv" : url = "/SecureInspect/site/advSearchfile"; data = $("#searchForm").serialize();
		break;
	}
	$.ajax({
		type : "POST",
		async : false,
		cache : false,
		data : data,
		url : url,
		dataType : "html",
		success : function(result) {
			$("." + type + "Div").html(result);
			$("." + type + "Div input[name=byPageFlag]").val(byPageFlag);
			var wrap = $("." + type + "Div")[0];
			var totalPageNum = $("." + type + "Div input[name=totalPageNum]").val();
			var curPage = $("." + type + "Div input[name=curPage1]").val();
			setPage(curPage, totalPageNum, 10, wrap);
			showRedKeyInPage($("." + type + "Div"));
			setTypeShowStyle($("." + type + "Div"));
		},
		error : function(error) {
			console.log(error);
		}
	});
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

function batchDelete(){
	var siteIdList = [];
	var itemList = document.getElementsByName("itemCk");
	
	for(var i=0; i<itemList.length; i++){
		if (itemList[i].checked){
			siteIdList.push(itemList[i].value);
		}
	}
	var url = "/SecureInspect/site/batchDelete";
	$.ajax({
		cache:false,
		sync: false,
		type:"post",
		data: {"siteIdList":JSON.stringify(siteIdList)},
		url:url,
		dataType:"text",
		success:function(data){
			if(data == "ok"){
				console.log("删除成功");
			}
			else{
				console.log("删除失败");
			}
		},
		error:function(e){
			console.log(e);
		}
	});
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
	var url = "/SecureInspect/site/genReport";
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
	var url = "/SecureInspect/site/sendObjectArr";
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
</script>
<div class="rightWrap">
<div class="navDiv">
	<a class="siteSearch" name="site" onclick="loadSite()">网站查询</a>
	<a class="pageSearch" name="page" onclick="loadPage()">网页查询</a>
	<a class="fileSearch" name="file" onclick="loadFile()">文件查询</a>
</div>
<div class="downDoc"></div>
<div class="functionDiv">
	<form id="searchForm">
		<input type="hidden" name="briefSrchKey">
		<input type="hidden" name="briefSrchVal">
		<input type="hidden" name="type">
		<input type="hidden" name="startTime">
		<input type="hidden" name="endTime">
		<input type="hidden" name="location">
		<input type="hidden" name="fileName">
		<input type="hidden" name="pageSize" value="10">
		<input type="hidden" name="secCondition">
		<input type="hidden" name="position">
		<input type="hidden" name="curPage">
		<input type="hidden" name="taskType" value="site">
	</form>
	<select class="searchSelect">
		<option value="task_name">网站名称</option>
		<option value="keyword">关键字</option>
	</select>
	<input type="text" class="searchInput">
	<input type="button" value="检索" class="btnSrch" onclick="briefSearch()">
</div>
<div class="advSrch">
	<div class="timeDiv">
		<span class="tip">时间</span>
		<input name="startDate" id="startTime" type="text"  onclick="calendar.show(this);" readonly="readonly">
		<span class="to">-</span>
		<input name="endDate" id="endTime" type="text"  onclick="calendar.show(this);" readonly="readonly">
	</div>
	<div class="siteName">
		<span class="tip">网站地址</span>
		<input type="text" name="location" class="site">
	</div>
	<!-- <div class="resourceFileDiv">
		<span class="tip">资源名称</span>
		<input type="text" name="title" class="titleName">
	</div> -->
	<div class="itemNum">
		<span class="tip">每页条数</span>
		<select name="pageSize" class="pageSize">
			<option value="10">每页显示10条</option>
			<option value="20">每页显示20条</option>
			<option value="30">每页显示30条</option>
		</select>
	</div>
	<div class="iresult">
		<span class="tip">涉密鉴定</span>
		<select name="secCondition" class="secCondition">
			<option value="secret">是</option>
			<option value="no_secret">否</option>
			<option value="wait">未鉴定</option>
			<option value="suspect">可疑</option>
		</select>
	</div>
	<div class="advSrchBtn"><input type="button" value="高级检索" onclick="advSearch()"></div>
</div>
<div class="siteDiv listDiv"></div>
<div class="pageDiv listDiv"></div>
<div class="fileDiv listDiv"></div>
</div>
