<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<input type="hidden" name="totalPageNum" value="${pageView.totalPageNum}">
<input type="hidden" name="curPage" value="${pageView.curPage}">
<input type="hidden" name="byPageFlag">
<input type="hidden" name="pageId" value="${pageId}">
<input type="hidden" name="pageSize" value="${pageView.pageSize}">
<ul>
	<li class="funcLi"><!-- <a class="delete">删除</a> --><!-- <a class="export">导出</a></li> -->
	<li>
		<span class="all"><input id="batch" type="checkbox"><label for="batch">全选</label></span>
		<!-- <span class="index">序号</span> -->
		<span class="title">文件名称</span>
		<span class="URL head">在线</span>
		<span class="fileloc">源文件</span>
		<span class="secureCnt">涉密次数</span>
		<span class="secureResult">涉密鉴定</span>
		<span class="type">类型</span>
		<span class="operation">操作</span>
		<span class="kwd">命中关键字</span>
		<span class="context">上下文</span>
	</li>
	<c:forEach var="file" items="${pageView.items}" varStatus="status">
	<li class="licon">
		<input type="hidden" class="pageId" value="${file.fileId}">
		<span class="all"><input id="batchDel"${status.index} type="checkbox"><label for="batchDel"${status.index}></label>${pageView.pageSize * (pageView.curPage-1) + status.index + 1}</span>
		<%-- <span class="index">${pageView.curPage}.${pageView.pageSize * (pageView.curPage-1) + status.index + 1}</span> --%>
		<span class="title">${file.title}</span>
		<a class="URL" href="${file.URL}" title="${file.URL}" target="_blank">查看</a>
		<a class="fileloc" href="/SecureInspect/preview/downSrcFile/${file.fileId}">下载</a>
		<span class="secureCnt">${file.secureCnt}</span>
		<span class="secureResult">
			<c:if test="${file.iresult=='secret'}"><c:out value="涉密"></c:out></c:if>
			<c:if test="${file.iresult=='suspect'}"><c:out value="可疑"></c:out></c:if>
			<c:if test="${file.iresult=='no_secret'}"><c:out value="非涉密"></c:out></c:if>
			<c:if test="${file.iresult=='wait'}"><c:out value="未处理"></c:out></c:if>
		</span>
		<span class="type">${file.type}</span>
		<span class="operation">
			<span><input class="handle" type="button" onclick="dealContext(this)" value="处理"></span>
		</span>
		<span class="kwd">
		<c:forEach var="kwdd" items="${file.keyContextList}" varStatus="statusi">
		${statusi.index+1}.${kwdd.key}<br>
		</c:forEach>
		</span>
		<span class="context">
		<c:forEach var="contextt" items="${file.keyContextList}" varStatus="statusj">
		<input type="hidden" name="key" value="${contextt.key}">
		<input type="hidden" name="context_id" value="${contextt.context_id}">
		<b class="contextB">${statusj.index+1}</b>.<span class="contextSpan">${contextt.context}</span>
		<c:if test ="${!statusj.last}"><hr></c:if>
		</c:forEach>
		</span>
	</li>
	</c:forEach>
</ul>
