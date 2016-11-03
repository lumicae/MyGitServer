<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务管理</title>
<link rel="stylesheet" type="text/css" href="/SecureInspect/css/taskHome.css"/>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery.form.js"></script>
<script type="text/javascript" src="/SecureInspect/js/pageTool.js"></script>
<script type="text/javascript" src="/SecureInspect/js/task/taskHome.js"></script>
</head>
<body>
<div class="taskWrap">
	<input type="hidden" value="${taskType}">
	<div class="taskTab">
		<a class="tabWeb clk" name="site" onclick="loadTask(this);">网站任务</a>
		<a class="tabWeibo" name="weibo" onclick="loadTask(this);">微博任务</a>
		<a class="TabWeChat" name="weixin" onclick="loadTask(this);">微信任务</a>
		<a class="TabEmail" name="email" onclick="loadTask(this);">邮箱任务</a>
	</div>
	<div class="taskContent">
		<div class="rightWrap">
			<div class="downDoc"></div>
			<a id="addTask">新增任务</a>
			<iframe name="iframe" style="display: none;"></iframe>
			<ul class="addUl">
				<li>
					<span>单位名称：</span><input type="text" name="taskName" id="taskName">
				</li>
				<form:form id="infoForm" modelAttribute="TaskRecPara" enctype="multipart/form-data" method="POST" target="iframe">
					<input type="hidden" name="sitename">
					<input type="hidden" name="kwds">
					<input type="hidden" name="siteurl">
					<input type="hidden" name="type">
				<li class="locationLi">
				</li>
				</form:form>
				<li class="kwdLi">
					<span>关键字：</span>
					<c:forEach var="kwd" items="${keywordList}">
					<input class="checkBoxK" type="checkbox"  name="kwd" value="${kwd.value}"><label>${kwd.value}</label>
					<input type="hidden" name="kwdId" value="${kwd.id}">
					</c:forEach>
				</li>
				<li class="btnLi">
					<input type="button" onclick="addTask()" value="增加"><input type="button" onclick="cancel()" value="取消">
				</li>
			</ul>
			<a class="listBtn">任务列表</a>
			<div class="listWrap">
			</div>
		</div>
	</div>
</div>
</body>
</html>