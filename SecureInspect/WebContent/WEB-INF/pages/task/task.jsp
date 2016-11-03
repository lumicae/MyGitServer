<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/SecureInspect/js/pageTool.js"></script>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
</head>
<style type="text/css">
a, span{
	font-family:microsoft yahei;
	font-size:18px;
	text-align:center;
}
ul,li{
	padding:0px;
	margin:0px;
}
ul li{
	list-style:none;
}
.rightWrap{
	width:100%;
	height:100%;
}
#addTask, .listBtn{
	background-color:#ddd;
	width:150px;
	display:inline-block;
    height: 30px;
    line-height: 30px;
    border-radius: 10px;
}
.addUl li{
	height:50px;
	line-height:60px;
}
.addUl li input{
	height: 30px;
    line-height: 30px;
    font-size:18px;
    margin-left:20px;
}
.addUl li input[type=text]{
	width:600px;
}
.addUl li:last-child input{
	margin-left:104px;
}
.addUl li span{
	width:100px;
	display:inline-block;
}
.addUl li.kwdLi input{
	vertical-align:middle;
}

.addUl li.kwdLi label{
	display:inline-block;
	line-height:25px;
	text-align:center;
	vertical-align:middle;
}
.listWrap{
	width:1035px;
}
.kwdUl li span:last-child input{
	font-size: 18px;
	margin-top:7px;
}
.kwdUl li{
	height:40px;
	line-height:40px;
	border-bottom:1px solid #ddd;
	width:1350px;
}
.kwdUl li span{
	display:inline-block;
	text-overflow: ellipsis;
    height: 40px;
    overflow: hidden;
}
.kwdUl li .url{
	width:400px;
}
.kwdUl li .name{
	width:350px;
}
.kwdUl li .index{
	width:50px;
}
.kwdUl li .keys{
	width:200px;
	text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}
.kwdUl li .time{
	width:120px;
}
.kwdUl li .opt{
	width:60px;
}
.kwdUl li .status{
	width:80px;
}
.kwdUl li span{
	display:inline-block;
	width:107px;
}
#pageDiv{
	margin-top:20px;
    width: 1035px;
	text-align:center;
}
#pageDiv a{
	text-decoration:none;
	color: #000;
    margin-left: 5px;
}
</style>
<script type="text/javascript">

	 $(function(){
		var totalPageNum = $("input[name=totalPageNum]").val();
		var curPage = $("input[name=curPage]").val();
		var wrap = $(".listWrap")[0];
		setPage(curPage, totalPageNum, 10, wrap);
		
		/* var myDate1 = new Date();
		console.log("ready" + myDate1.getMilliseconds());
		window.setTimeout(wait,5000); */
		window.setInterval(taskProcess,30000);
	});
	 
	function wait(){
		var myDate2 = new Date();
		console.log("start" + myDate2.getMilliseconds());
		var url = "/SecureInspect/mvc/wait";
		$.ajax({
			async : false,
			cache : false,
			type : "get",
			url : url,
			dataType : "text",
			success:function(result){
				console.log(result);
				var myDate3 = new Date();
				console.log("end" + myDate3.getMilliseconds());
			},
			error:function(msg){
				console.log(msg);
			}
		});
	}
	
	 function taskProcess(){
		 var list = $(".kwdUl .content");
		 
		 for(var i=0; i<list.size(); i++){
			 var li = list.get(i);
			 var obj = {};
			 var status = $(li).find(".status").html();
			 if(status != "处理中" && status !="检查结束"){
				 obj.name = $(li).find(".name").html();
				
				 var url = "/SecureInspect/mvc/taskProcess";
				 $.ajax({
					async : false,
					cache : false,
					type : "POST",
					data: {"obj":JSON.stringify(obj)},
					url : url,
					dataType : "json",
					success : function(data) {
						console.log(data.status);
						if(data.status == "True"){
							console.log($(li).find(".status").html());
							$(li).find(".status").html("处理中");
							console.log($(li).find(".status").html());
							console.log($(li).find(".name").html());
							var urlo = "/SecureInspect/mvc/saveResult";
							$.ajax({
								async:false,
								cache:false,
								type:"post",
								url:urlo,
								data:{"taskname":data.name},
								dataType:"text",
								success:function(result){
									$(li).find(".status").html("检查结束");
								},
								error:function(msg){
									console.log(msg);
								}
							});
						}
						else if(data.num != "0"){
							if($(li).find(".status").html() == "未开始"){
								var urlu = "/SecureInspect/mvc/updateStatus";
								$.ajax({
									async:false,
									cache:false,
									type:"post",
									url:urlu,
									data:{"taskname":data.name},
									dataType:"text",
									success:function(result){
										console.log("正在执行任务");
									},
									error:function(msg){
										console.log(msg);
									}
								});
							}
							$(li).find(".status").html(data.num);
						}
					},				
					error : function(msg) {
						console.log(msg);
					}
				 });
			 }
		 }
	 }
	 
	 function exportReport(obj){
		var url = "/SecureInspect/mvc/genReport";
		var downDiv = document.getElementsByClassName("downDoc");
		
		var iframe = document.createElement("iframe");
		iframe.name = "dIframe";
		iframe.setAttribute("style","display:none");
		downDiv[0].appendChild(iframe);
		
		var form = document.createElement("form");
		form.setAttribute("style", "display:none");
		form.setAttribute("target", "dIframe");
		form.name= "dForm";
		var input1 = document.createElement("input");
		input1.name="siteId";
		input1.type="hidden";
		input1.value=$(obj).parents("li").find(".siteId").val();
		var input2 = document.createElement("input");
		input2.name="siteName";
		input2.type="hidden";
		input2.value=$(obj).parents("li").find(".name").html()
		form.appendChild(input1);
		form.appendChild(input2);
		downDiv[0].appendChild(form);
		form.action = url;
		form.submit();
	 }
	 
	 function getDataByPage(clickPage){
		 var url = "/SecureInspect/task/task0/" + clickPage;
		 $.ajax({
			async : false,
			cache : false,
			type : "GET",
			url : url,
			dataType : "html",
			success : function(data) {
				$(".listWrap").html(data);
				var pageSize = 10;
				var totalPageNum = $("input[name=totalPageNum]").val();
				var curPage = $("input[name=curPage]").val();
				var wrap = $(".listWrap")[0];
				
				setPage(curPage, totalPageNum, pageSize, wrap);
				window.setInterval(taskProcess,1000);
			},				
			error : function(msg) {
				console.log(msg);
			}
		 });
	 }
	 
	function addTask(){
		var kl = document.getElementsByName("kwd");
		var obj = {};
		var keys = ""
		for(var i=0;i<kl.length;i++){
			if (kl[i].checked){
				keys += "," + kl[i].value;
			}
		}
		keys = keys.substr(1);
		obj.kwds = keys;
		obj.sitename = $("#siteName").val();
		obj.siteurl = $("#siteUrl").val();
		obj.type="sitecheck";
		obj.dirname="";
		if(obj.siteUrl == "" || obj.siteName=="" || obj.kwds == ""){
			alert("请将信息填写完整");
			return;
		}
		var url = "/SecureInspect/task/addTask";
		$.ajax({
			cache:false,
			sync:false,
			type:"post",
			data: {"obj":JSON.stringify(obj)},
			url:url,
			dataType:"html",
			success:function(data){
				$(".listWrap").html(data);
				var flag=$("#flag").val();
				if(flag == "no"){
					alert("任务添加失败");
				}
				var pageSize = 10;
				var totalPageNum = $("input[name=totalPageNum]").val();
				var curPage = $("input[name=curPage]").val();
				var wrap = $(".listWrap")[0];
				
				setPage(curPage, totalPageNum, pageSize, wrap);
				window.setInterval(taskProcess,1000);
			},
			error:function(e){
				console.log(e);
			}
		});
	}
	function cancel(){
		$("#siteUrl").val("");
		$("#siteName").val("");
		$(".checkBoxK").attr("checked", false);
	}
	function cancelTask(item){
		taskId= $(item).parents("li").find(".taskId").val();
		siteId = $(item).parents("li").find(".siteId").val();
		var url = "/SecureInspect/task/cancelTask";
		$.ajax({
			cache:false,
			sync:false,
			type:"post",
			data: {"taskId":taskId, "siteId": siteId},
			url:url,
			dataType:"text",
			success:function(data){
				if(data == "ok"){
					$(item).val("激活");
					$(item).parents("li").find(".status").html("已取消");
					$(item).attr("onclick", "restartTask(this)");
				}
				if(data == "no"){
					alert("任务添加失败");
				}
			},
			error:function(e){
				console.log(e);	
			}
		});
	}
	function restartTask(item){
		var obj = {};
		obj.taskId= $(item).parents("li").find(".taskId").val();
		obj.siteId = $(item).parents("li").find(".siteId").val();
		obj.kwds = $(item).parents("li").find(".keys").val();
		obj.siteName = $(item).parents("li").find(".name").val();
		obj.siteUrl = $(item).parents("li").find(".url").val();
		var url = "/SecureInspect/task/restartTask";
		$.ajax({
			cache:false,
			sync:false,
			type:"post",
			data: {"obj": JSON.stringify(obj)},
			url:url,
			dataType:"text",
			success:function(data){
				if(data == "ok"){
					$(item).val("取消");
					$(item).parents("li").find(".status").html("检查中");
					$(item).attr("onclick", "cancelTask(this)");
				}
				if(data == "no"){
					alert("任务添加失败");
				}
			},
			error:function(e){
				console.log(e);
			}
		});
	}
</script>
<body>
	<div class="rightWrap">
	<div class="downDoc"></div>
		<a id="addTask">新增任务</a>
		<ul class="addUl">
			<li>
				<span>网站名称：</span><input type="text" name="siteName" id="siteName">
			</li>
			<li>
				<span>网站URL：</span><input type="text" name="siteUrl" id="siteUrl">
			</li>
			<li class="kwdLi">
				<span>关键字：</span>
					<c:forEach var="kwd" items="${keywordList}">
					<input class="checkBoxK" type="checkbox"  name="kwd" value="${kwd.value}"><label>${kwd.value}</label>
					<input type="hidden" name="kwdId" value="${kwd.id}">
					</c:forEach>
			</li>
			<li>
				<input type="button" onclick="addTask()" value="增加"><input type="button" onclick="cancel()" value="取消">
			</li>
		</ul>
		<a class="listBtn">任务列表</a>
		<div class="listWrap">
			<input type="hidden" name="curPage" value="${pageview.curPage}">
			<input type="hidden" name="totalPageNum" value="${pageview.totalPageNum}">
			<ul class="kwdUl">
				<li class="head">
					<span class="index">序号</span>
					<span class="url">网址</span>
					<span class="name">名称</span>
					<span class="keys">关键字</span>
					<span class="time">添加时间</span>
					<span class="status">状态</span>
					<span class="opt">操作</span>
				</li>
				<c:forEach var="task" items="${pageview.items}" varStatus="status">
				<li class="content">
					<input type="hidden" value="${task.taskId}" class="taskId">
					<input type="hidden" value="${task.siteId}" class="siteId">
					<input type="hidden" value="${dfd}" class="taskProcess">
					<span class="index">${(pageview.curPage-1)*pageview.pageSize + status.index + 1}</span>
					<span class="url">${task.siteUrl}</span>
					<span class="name" title="${task.siteName}">${task.siteName}</span>
					<span class="keys" title="${task.kwds}">${task.kwds}</span>
					<span class="time">${task.time}</span>
					<span class="status">${task.status}</span>
					<span class="opt">
						<%-- <c:if test="${task.status=='已取消'}"><input type="button" value="激活" onclick="restartTask(this);"></c:if>
						<c:if test="${task.status=='未开始' || task.status =='检查中'}"><input type="button" value="取消" onclick="cancelTask(this);"></c:if>
						<c:if test="${task.status == '检查结束'}"><input type="button" value="导出" onclick="exportReport(this);"></c:if> --%>
						<input type="button" value="导出" onclick="exportReport(this);">
					</span>
				</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	
</body>
</html>