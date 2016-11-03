/**
 * 
 */
$(function(){
	$(".taskTab a").each(function(index,ev){
		$(ev).click(function(){
			$(".taskTab a").removeClass("clk");
			$(ev).addClass("clk");
		})
	});
	loadTask($("a[name=site]"));
	adapWrapSize();
	$(window).resize(function(){
		adapWrapSize();
	});
	
	window.setInterval(taskProcess,30000);
});
function adapWrapSize(){
	 var page_height = $(window).height();
	var wrap_height = page_height - 380;
	$(".taskWrap").css({"height" : page_height-20});
}
function loadTask(item){
	
	var taskType = $(item).attr("name");
	console.log(taskType);
	if(taskType == "site"){
		var html = "<span>网站地址：</span><input type='text' id='location' name='location'>";
		$(".locationLi").html(html);
	}
	else if(taskType == "weibo"){
		var html = "<span>政务微博公众号地址：</span><input type='text' id='location' name='location'>";
		$(".locationLi").html(html);
	}
	else if(taskType == "weixin"){
		var html = "<span>微信公众号地址：</span><input type='text' id='location' name='location'>";
		$(".locationLi").html(html);
	}
	else if(taskType == "email"){
		var html = "<span>邮件压缩包：</span>" +
				"<iframe name='iframe' style='display: none;'></iframe>" + 
				"<form:form id='infoForm' enctype='multipart/form-data' method='POST' target='iframe'>" +
				"<input type='file' id='file' name='file'>" +
				"</form:form>" +
				"<input type='hidden' id='location' name='location'>";
		$(".locationLi").html(html);
	}
	var url = "/SecureInspect/task/loadTask/" + taskType;
	$.ajax({
		cache:false,
		sync:false,
		type:"get",
		url:url,
		dataType:"html",
		success:function(data){
			$(".listWrap").html(data);
			var totalPageNum = $("input[name=totalPageNum]").val();
			var curPage = $("input[name=curPage]").val();
			var wrap = $(".listWrap")[0];
			setPage(curPage, totalPageNum, 10, wrap);
		},
		error:function(e){
			console.log(e);
		}		
	});
}

 function sendTaskProcessRequest(li){
	 var taskType = $(".taskTab .clk").attr("name");
		console.log(taskType);
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
					data:{"validFlag":validFlag, "taskid":data.id},
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
						data:{"taskid":data.id,"type":taskType},
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
	console.log(list.size());
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
	var url = "/SecureInspect/preview/genReport";
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
	input2.name="taskName";
	input2.type="hidden";
	input2.value=$(obj).parents("li").find(".name").html()
	form.appendChild(input1);
	form.appendChild(input2);
	downDiv[0].appendChild(form);
	form.action = url;
	form.submit();
 }
 
 function getDataByPage(clickPage){
	 var taskType = $(".taskTab .clk").attr("name");
	 var url = "/SecureInspect/task/task0/" + clickPage + "/" + taskType;
	 $.ajax({
		async : false,
		cache : false,
		type : "GET",
		url : url,
		dataType : "html",
		success : function(data) {
			$(".listWrap").html(data);
			var totalPageNum = $("input[name=totalPageNum]").val();
			var curPage = $("input[name=curPage]").val();
			var wrap = $(".listWrap")[0];
			
			setPage(curPage, totalPageNum, 10, wrap);
		},				
		error : function(msg) {
			console.log(msg);
		}
	 });
 }
 
function addTask(){
	var taskType = $(".taskTab .clk").attr("name");
	console.log(taskType);
	var kl = document.getElementsByName("kwd");
	var obj = {};
	var keys = ""
	for(var i=0;i<kl.length;i++){
		if (kl[i].checked){
			keys += "," + kl[i].value;
		}
	}
	keys = keys.substr(1);
	$("input[name=kwds]").val(keys);
	$("input[name=sitename]").val($("#taskName").val());
	$("input[name=siteurl]").val($("#location").val())
	$("input[name=type]").val(taskType);
	var url = "";
	if(taskType == "email") {
		$("input[name=siteurl]").val("emailTask");
		url = "/SecureInspect/task/addEmailTask?tempid=" + Math.random();
	}
	else{
		url = "/SecureInspect/task/addTask?tempid=" + Math.random();
	}
	if($("input[name=siteurl]").val() == "" ||$("input[name=sitename]").val() =="" || $("input[name=kwds]").val() == ""){
		alert("请将信息填写完整");
		return;
	}

	var form = $('#infoForm')[0];
	form.action = url;
	form.submit();
}

function uploadFileCallback(result){
	if(result=="ok"){
		getDataByPage(0);
	}
	else{
		alert(result);
	}
}
function showResponse(data){
	$(".listWrap").html(data);
	var flag=$(".listWrap #flag").val();
	if(flag == "no"){
		alert("任务添加失败");
	}
	var pageSize = 10;
	var totalPageNum = $("input[name=totalPageNum]").val();
	var curPage = $("input[name=curPage]").val();
	var wrap = $(".listWrap")[0];
	
	setPage(curPage, totalPageNum, pageSize, wrap);
}
function cancel(){
	$("#location").val("");
	$("#taskName").val("");
	$("#file").val("");
	$(".checkBoxK").attr("checked", false);
}