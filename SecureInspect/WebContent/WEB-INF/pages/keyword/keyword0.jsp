<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<input type="hidden" name="totalPageNum" value="${pageview.totalPageNum}">
<input type="hidden" name="curPage" value="${pageview.curPage}">
<input type="hidden" name="pageSize" value="${pageview.pageSize}">
<ul class="kwdUl">
	<li class="head">
		<span>序号</span>
		<span>关键字</span>
		<span>密级</span>
	</li>
	<c:forEach var="keyword" items="${pageview.items}" varStatus="status">
	<li class="content">
	<span class="index">${(pageview.curPage-1) * pageview.pageSize + status.index + 1}</span>
	<span>${keyword.value}</span>
	<span>${keyword.rank}</span>
	</li>
	</c:forEach>
</ul>