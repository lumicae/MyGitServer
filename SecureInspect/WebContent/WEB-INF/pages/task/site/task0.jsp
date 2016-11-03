<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<input type="hidden" name="curPage" value="${pageview.curPage}">
<input type="hidden" name="totalPageNum" value="${pageview.totalPageNum}">
<input type="hidden" id="flag" value="${flag}">
<ul class="kwdUl">
	<li class="head">
		<span class="index">序号</span>
		<span class="name">名称</span>
		<span class="url">网址</span>
		<span class="keys">关键字</span>
		<span class="time">添加时间</span>
		<span class="status">状态</span>
		<span class="opt">操作</span>
	</li>
	<c:forEach var="task" items="${pageview.items}" varStatus="status">
	<li class="content">
	<input type="hidden" value="${task.taskId}" class="taskId">
	<span class="index">${pageview.pageSize * (pageview.curPage-1) + status.index + 1}</span>
	<span class="name" title="${task.taskName}">${task.taskName}</span>
	<span class="url">${task.location}</span>
	<span class="keys">${task.kwds}</span>
	<span class="time">${task.time}</span>
	<span class="status">${task.status}</span>
	<span class="opt">
		<input type="button" value="导出" onclick="exportReport(this);">
	</span>
	</li>
	</c:forEach>
</ul>
