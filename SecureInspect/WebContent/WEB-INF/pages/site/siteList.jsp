<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
	<input type="hidden" name="totalPageNum" value="${pageView.totalPageNum}">
	<input type="hidden" name="curPage1" value="${pageView.curPage}">
	<input type="hidden" name="byPageFlag">
	<ul>
		<li class="funcLi"><!-- <a class="delete">删除</a> --><a class="export" onclick="exportReport()">导出</a></li>
		<li>
			<span class="all"><input id="batch" type="checkbox" onclick="checkAll()"><label for="batch" style="cursor:pointer">全选</label></span>
			<span class="index">序号</span>
			<span class="name">网站名称</span>
			<span class="URL head">网址</span>
			<span class="time">时间</span>
			<span class="ratio">涉密数/页面总数</span>
			<!-- <span class="operation">操作</span> -->
			<span class="detail">详情</span>
			<span class="status">状态</span>
		</li>
		<c:forEach items="${pageView.items}" var="item" varStatus="status">
		<li>
			<input type="hidden" class="taskId" value="${item.taskId}">
			<span class="all"><input id="batchDel"${status.index} type="checkbox" name="itemCk" class="itemCk"><label for="batchDel"${status.index}></label></span>
			<span class="index">${pageView.pageSize * (pageView.curPage-1) + status.index + 1}</span>
			<span class="name">${item.name}</span>
			<a class="URL" href="${item.location}" target="_blank">${item.location}</a>
			<span class="time"><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd"/>至
			<c:if test="${item.status =='检查结束'}">
			<fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd"/>
			</c:if>
			<c:if test="${item.status !='检查结束'}">
			--
			</c:if>
			</span>
			<span class="ratio">${item.secureCnt}/${item.total}</span>
			<!-- <span class="operation"><input type="button" value="修改" class="modify" onclick="modify(this)"></span> -->
			<span class="detail"><input type="button" value="展开" onclick="getPageList(this, 1)"></span>
			<span class="status">${item.status}</span>
		</li>
		</c:forEach>
	</ul>
