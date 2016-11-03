<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Web保密检查系统V1.0</title>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
<link rel="stylesheet" type="text/css" href="/SecureInspect/css/mainPage.css"/>
<script type="text/javascript">
$(function(){
	if(window != top){
		top.location.href = location.href;
	}
	
	resizeBody();
	$(window).resize(function(){
		resizeBody();
	});
	$(".leftWrap ul li a").each(function(){
		var t = $(this);
		$(t).on("click", function(){
			$(".leftWrap ul li a").removeClass("clk");
			$(t).addClass("clk");
		});
	});
	showSite();
	$(".leftWrap ul li:first a").click();
		
});
function resizeBody(){
	var window_height = $(window).height();
	var window_width = $(window).width();
	$(".down").height(window_height - 100);
}
function showSite(){
	var url = "http://" + location.host + "/SecureInspect/preview/previewHome/site";
	$("#rightAreaIframe").attr("src", url);
}
function showUser(){
	var url = "http://" + location.host + "/SecureInspect/account/user";
	$("#rightAreaIframe").attr("src", url);
}
function showKeyword(){
	var url = "http://" + location.host + "/SecureInspect/keyword/keyword";
	$("#rightAreaIframe").attr("src", url);
}
function showTask(){
	var url = "http://" + location.host + "/SecureInspect/task/taskHome";
	$("#rightAreaIframe").attr("src", url);
}
function showHelp(){
	var url = "http://" + location.host + "/SecureInspect/account/help";
	$("#rightAreaIframe").attr("src", url);
}
function showWeibo(){
	var url = "http://" + location.host + "/SecureInspect/preview/previewHome/weibo";
	$("#rightAreaIframe").attr("src", url);
}
function showWeixin(){
	var url = "http://" + location.host + "/SecureInspect/preview/previewHome/weixin";
	$("#rightAreaIframe").attr("src", url);
}
function showEmail(){
	var url = "http://" + location.host + "/SecureInspect/preview/previewHome/email";
	$("#rightAreaIframe").attr("src", url);
}
</script>
</head>
<body>
	<div class="wrap">
		<div class="title">
			<img class="titleImg" src="/SecureInspect/images/check.png">
			<div class="titleText">
				<span class="titleCh">Web保密检查系统V1.0</span>
				<span class="titleEn">Web Security Inspector System V1.0</span>
			</div>
		</div>
		<div class="down">
			<div class="left">
				<div class="leftWrap">
					<ul>
						<li><a href="javascript:void(0);" onclick="showSite()">网站查询</a></li>
						<li><a href="javascript:void(0);" onclick="showWeibo()">微博查询</a></li>
						<li><a href="javascript:void(0);" onclick="showWeixin()">微信查询</a></li>
						<li><a href="javascript:void(0);" onclick="showEmail()">邮箱查询</a></li>
						<li><a href="javascript:void(0);" onclick="showUser()">用户信息</a></li>
						<li><a href="javascript:void(0);" onclick="showKeyword()">关键字管理</a></li>
						<li><a href="javascript:void(0);" onclick="showTask()">任务管理</a></li>
						<li><a href="javascript:void(0);" onclick="showHelp()">帮助</a></li>
					</ul>
				</div>
				
			</div>
			<div class="right">
			<iframe  style="width:100%;height:100%;" id="rightAreaIframe" frameborder="0" scrolling="no"></iframe>
			</div>
		</div>
	</div>
</body>
</html>