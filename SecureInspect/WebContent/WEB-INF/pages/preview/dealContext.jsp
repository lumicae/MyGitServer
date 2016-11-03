<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
.back{
    width: 60%;
    height: 60%;
    background-color: #fff;
    position: absolute;
    top: 20%;
    left: 20%;
    overflow:auto;
}
.titleDiv{
    height: 50px;
    line-height: 50px;
    text-align: center;
    font-size:20px;
    color:#000;
}
.titleDiv .titleA{
	margin-left:20px;
	cursor:pointer;
	color:blue;
}
.divUl{
    width: 80%;
    margin: auto;
    font-size:17px;
}
.divUl ul{
	width:100%;
}
.divUl ul li{
	border-top:1px solid gray;
	border-bottom:1px solid gray;
}
li span{
	width:24%;
	display:inline-block;
	height:auto;
	line-height:30px;
	text-align:center;
	vertical-align:middle;
}

.contextSpan{
	text-align:left;
}

li span input{
	width:29px;
	vertical-align:middle;
	cursor:pointer;
}
ul,li div, span{
	color:#000;
}
.divBtn{
	text-align:center;
	margin-top:20px;
	height:50px;
}
.divBtn input{
	font-size:20px;
}
.divBtn input:first-child{
	margin-right:40px;
}
</style>
<div class="back">
	<div class="titleDiv"><a class="titleA">${file_name}</a></div>
	<input type="hidden" value="${file_id}" name="fileId">
	<div class="divUl">
		<ul>
			<li>
				<span>序号</span>
				<span>关键字</span>
				<span>上下文</span>
				<span>涉密判定</span></li>
			
			<c:forEach items="${keyContextList}" var="item" varStatus="status">
			<li>
				<input type="hidden" name="context_id" value="${item.context_id}">
				<span>${status.index + 1}</span>
				<span class="key">${item.key}</span>
				<span class="contextSpan">${item.context}</span>
				<span>
					<c:if test="${item.result == 'secret'}">
					<input name="result${status.index + 1}" class="result" type="radio" value="yes" checked="checked">是
					<input name="result${status.index + 1}" class="result" type="radio" value="no">否
					<input name="result${status.index + 1}" class="result" type="radio" value="suspect">可疑
					</c:if>
					<c:if test="${item.result == 'no_secret'}">
					<input name="result${status.index + 1}" class="result" type="radio" value="yes">是
					<input name="result${status.index + 1}" class="result" type="radio" value="no" checked="checked">否
					<input name="result${status.index + 1}" class="result" type="radio" value="suspect">可疑
					</c:if>
					<c:if test="${item.result == 'suspect'}">
					<input name="result${status.index + 1}" class="result" type="radio" value="yes">是
					<input name="result${status.index + 1}" class="result" type="radio" value="no">否
					<input name="result${status.index + 1}" class="result" type="radio" value="suspect" checked="checked">可疑
					</c:if>
					<c:if test="${item.result == 'wait'}">
					<input name="result${status.index + 1}" class="result" type="radio" value="yes">是
					<input name="result${status.index + 1}" class="result" type="radio" value="no">否
					<input name="result${status.index + 1}" class="result" type="radio" value="suspect" checked="checked">可疑
					</c:if>
				</span>
			</li>
			</c:forEach>
		</ul>
	
	</div>
	<div class="divBtn"><input class="save" type="button" value="保存"><input class="close" type="button" value="取消"></div>
</div>