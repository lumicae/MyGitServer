<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<input type="hidden" name="totalPageNum" value="${pageView.totalPageNum}">
<input type="hidden" name="curPage1" value="${pageView.curPage}">
<input type="hidden" name="taskId" value="${taskId}">
<input type="hidden" name="pageSize" value="${pageView.pageSize}">
<ul class="fileUl">
	<li class="funcLi"><!-- <a class="delete">删除</a> --><!-- <a class="export">导出</a>-->
		
	</li> 
	<li>
		<span class="all"><input id="batch" type="checkbox"><label for="batch">全选</label></span>
		<span class="index">序号</span>
		<!-- <span class="name">资源名称</span> -->
		<span class="URL head">网址</span>
		<!-- <span class="fileloc">文件地址</span> -->
		<span class="secureCnt">涉密次数</span>
		<span class="secureResult">涉密鉴定</span>
		<span class="type">类型</span>
		<span class="operation">操作</span>
		<span class="kwd">命中关键字</span>
		<span class="context">上下文</span>
	</li>
	<c:forEach var="page" items="${pageView.items}" varStatus="status">
	<li class="licon">
		<input type="hidden" class="pageId" value="${page.pageId}">
		<span class="all"><input id="batchDel"${status.index} type="checkbox"><label for="batchDel"${status.index}></label></span>
		<span class="index">1.${pageView.pageSize * (pageView.curPage-1) + status.index + 1}</span>
		<%-- <span class="title">${page.title}</span> --%>
		<a class="URL" href="${page.URL}" target="_blank">${page.URL}</a>
		<%-- <span class="fileloc">${page.src}</span> --%>
		<span class="secureCnt">${page.secureCnt}</span>
		<span class="secureResult">
			<c:if test="${page.iresult=='secret'}"><c:out value="涉密"></c:out></c:if>
			<c:if test="${page.iresult=='suspect'}"><c:out value="可疑"></c:out></c:if>
			<c:if test="${page.iresult=='no_secret'}"><c:out value="非涉密"></c:out></c:if>
			<c:if test="${page.iresult=='wait'}"><c:out value="未处理"></c:out></c:if>
		</span>
		<span class="type">
			<input type="hidden" value="${page.containFileFlag}">
			<c:if test="${page.containFileFlag=='yes'}"><span>正文</span><span style="border-top:1px solid gray">文件</span></c:if>
			<c:if test="${page.containFileFlag=='no'}"><span>正文</span></c:if>
		</span>
		<span class="operation">
			<span><input class="handle" type="button" onclick="dealContext(this)" value="处理"></span>
			<c:if test="${page.containFileFlag=='yes'}"><span style="border-top:1px solid gray;"><input style="margin-top:10px;" type="button" onclick="getFileList(this, 1)" value="展开"></span></c:if>
		</span>
		<span class="kwd">
			<c:forEach var="kwdd" items="${page.keyContextList}" varStatus="statusi">
			${statusi.index+1}.${kwdd.key}<br>
			</c:forEach>
		</span>
		<span class="context">
			<c:forEach var="contextt" items="${page.keyContextList}" varStatus="statusj">
			<input type="hidden" name="key" value="${contextt.key}">
			<input type="hidden" name="key_id" value="${contextt.key_id}">
			<input type="hidden" name="context_id" value="${contextt.context_id}">
			<b class="contextB">${statusj.index+1}</b>.<span class="contextSpan">${contextt.context}</span>
			<c:if test ="${!statusj.last}"><hr></c:if>
			</c:forEach>
		</span>
	</li>
	</c:forEach>
</ul>