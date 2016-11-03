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
	height:60px;
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
	width:180px;
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
		var pageSize = 10;
		var totalPageNum = $("input[name=totalPageNum]").val();
		var curPage = $("input[name=curPage]").val();
		var wrap = $(".listWrap")[0];
		setPage(curPage, totalPageNum, pageSize, wrap);
		
		window.setInterval(taskProcess,30000);
	});
	
	 function sendTaskProcessRequest(li){
		 console.log("开始请求");
		 var date = new Date();
		 var obj = {};
		 obj.id = $(li).find(".taskId").val();
		 console.log(obj.id + "请求时间："+ date.getMinutes() + ":"+ date.getSeconds() + "." + date.getMilliseconds());
		 var url = "/SecureInspect/task/taskProcess";
		 $.ajax({
			async : false,
			cache : false,
			type : "POST",
			data: {"obj":JSON.stringify(obj)},
			url : url,
			dataType : "json",
			success : function(data) {
				 console.log(data.id + "返回时间："+ date.getMinutes() + ":"+ date.getSeconds() + "." + date.getMilliseconds());
				console.log(data.status);
				if(data.status == "True"){
					var validFlag = "valid";
					if(data.num == "0"){
						validFlag = "invalid";
					}
					console.log($(li).find(".status").html());
					$(li).find(".status").html("处理中");
					console.log($(li).find(".status").html());
					console.log($(li).find(".name").html());
					var urlo = "/SecureInspect/result/saveResult";
					$.ajax({
						async:false,
						cache:false,
						type:"post",
						url:urlo,
						data:{"type":"site","validFlag":validFlag, "taskid":data.id},
						dataType:"text",
						success:function(result){
							if(validFlag == "invalid"){
								$(li).find(".status").html("地址无效");
							}
							else{
								$(li).find(".status").html("检查结束");
							}
						},
						error:function(msg){
							console.log(msg);
						}
					});
				}
				else if(data.num != "0"){
					if($(li).find(".status").html() == "未开始"){
						var urlu = "/SecureInspect/task/updateStatus";
						$.ajax({
							async:false,
							cache:false,
							type:"post",
							url:urlu,
							data:{"taskid":data.id,"type":"site"},
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
	 
	 function taskProcess(){
		 var list = $(".kwdUl .content");
		 var date = new Date();
		console.log("新一轮开始时间："+ date.getMinutes() + ":"+ date.getSeconds() + "." + date.getMilliseconds());
		 for(var i=0; i<list.size(); i++){
			 var li = list.get(i);
			 var status = $(li).find(".status").html();
			 if(status != "处理中" && status !="检查结束"){
				 sendTaskProcessRequest(li);
			 }
			 if(i == list.size()/2){
				 sleep(2000);
			 }
		 }
	 }
	 function sleep(numberMillis) {  
		 var now = new Date();  
		 var exitTime = now.getTime() + numberMillis;  
		 while (true) {  
		 now = new Date();  
		 if (now.getTime() > exitTime)  
		 return;  
		 }  
	 }
	 
	 function exportReport(obj){
		var url = "/SecureInspect/site/genReport";
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
		input1.name="taskId";
		input1.type="hidden";
		input1.value=$(obj).parents("li").find(".taskId").val();
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
		 var url = "/SecureInspect/task/weiboTask0/" + clickPage;
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
		obj.type="weibo";
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
</script>
<body>
	<div class="rightWrap">
	<div class="downDoc"></div>
		<a id="addTask">新增任务</a>
		<ul class="addUl">
			<li>
				<span>单位名称：</span><input type="text" name="siteName" id="siteName">
			</li>
			<li>
				<span>政务微博公众号地址：</span><input type="text" name="siteUrl" id="siteUrl">
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
					<span class="name">单位名称</span>
					<span class="url">政务微博公众号</span>
					<span class="keys">关键字</span>
					<span class="time">添加时间</span>
					<span class="status">状态</span>
					<span class="opt">操作</span>
				</li>
				<c:forEach var="task" items="${pageview.items}" varStatus="status">
				<li class="content">
					<input type="hidden" value="${task.taskId}" class="taskId">
					<span class="index">${(pageview.curPage-1)*pageview.pageSize + status.index + 1}</span>
					<span class="name" title="${task.taskName}">${task.taskName}</span>
					<span class="url">${task.location}</span>
					<span class="keys" title="${task.kwds}">${task.kwds}</span>
					<span class="time">${task.time}</span>
					<span class="status">${task.status}</span>
					<span class="opt">
						<input type="button" value="导出" onclick="exportReport(this);">
					</span>
				</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	
</body>
</html>