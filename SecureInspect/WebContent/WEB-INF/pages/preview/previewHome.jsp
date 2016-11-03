<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="/SecureInspect/css/previewHome.css"/>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/SecureInspect/js/calendar/calendar.js"></script>
<script type="text/javascript" src="/SecureInspect/js/pageTool.js"></script>
<script type="text/javascript" src="/SecureInspect/js/preview/previewHome.js"></script>
<div class="rightWrap">
	<div class="siteNav navDiv">
		<a class="taskSearch" name="task" onclick="loadTask()">
			<c:if test="${taskType == 'site'}"><c:out value="网站查询"></c:out></c:if>
			<c:if test="${taskType == 'weibo'}"><c:out value="微博查询"></c:out></c:if>
			<c:if test="${taskType == 'weixin'}"><c:out value="微信查询"></c:out></c:if>
			<c:if test="${taskType == 'email'}"><c:out value="邮箱查询"></c:out></c:if>
		</a>
		<a class="pageSearch" name="page" onclick="loadPage()">
			<c:if test="${taskType != 'email'}"><c:out value="网页查询"></c:out></c:if>
			<c:if test="${taskType == 'email'}"><c:out value="邮件查询"></c:out></c:if>
		</a>
		<c:if test="${taskType == 'site'}">
			<a class="fileSearch" name="file" onclick="loadFile()">文件查询</a>
		</c:if>
		
	</div>

	<div class="downDoc"></div>

	<div class="functionDiv">
		<form id="searchForm">
			<input type="hidden" name="briefSrchKey">
			<input type="hidden" name="briefSrchVal">
			<input type="hidden" name="startTime">
			<input type="hidden" name="endTime">
			<input type="hidden" name="location">
			<input type="hidden" name="fileName">
			<input type="hidden" name="pageSize" value="10">
			<input type="hidden" name="secCondition">
			<input type="hidden" name="position">
			<input type="hidden" name="curPage" value="1">
			<input type="hidden" name="taskType" value="${taskType}"><!-- site,weibo,weixin,email -->
		</form>
		<select class="searchSelect">
			<option value="task_name">
				<c:if test="${taskType == 'site'}"><c:out value="单位名称"></c:out></c:if>
				<c:if test="${taskType == 'weibo' || taskType == 'weixin' || taskType == 'email'}"><c:out value="单位名称"></c:out></c:if>
			</option>
			
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
		<div class="taskName">
			<span class="tip">
				<c:if test="${taskType == 'site'}"><c:out value="网站地址"></c:out></c:if>
				<c:if test="${taskType == 'weibo'}"><c:out value="微博网址"></c:out></c:if>
				<c:if test="${taskType == 'weixin'}"><c:out value="微信网址"></c:out></c:if>
				<c:if test="${taskType == 'email'}"><c:out value="邮箱用户名"></c:out></c:if>
			</span>
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
	<input type="hidden" name="turnPageFlag">
	<div class="taskDiv listDiv"></div>
	<div class="pageDiv listDiv"></div>
	<div class="fileDiv listDiv"></div>
</div>
