<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/SecureInspect/js/pageTool.js"></script>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
</head>
<style type="text/css">
a,div, span{
	font-family:microsoft yahei;
	font-size:18px;
	text-align:center;
}
ul,li{
	padding:0;
	margin:0;
}
ul li{
	list-style:none;
}
.rightWrap{
	width:100%;
	height:100%;
	overflow:auto;
	text-align:left;
}
#addKwd, .listBtn{
	background-color:#ddd;
	width:150px;
	display:inline-block;
    height: 30px;
    line-height: 30px;
    border-radius: 10px;
}
.addUl{
	width:500px;
}
.addUl li{
	height:60px;
	line-height:60px;
}
.addUl li input{
	height: 30px;
    line-height: 30px;
    font-size:18px;
    margin-left:20px;
}
.addUl li:last-child input{
	margin-left:104px;
}
.addUl li span{
	width:75px;
	display:inline-block;
}
.kwdUl{
	width:600px;
	text-align:center;
}
.kwdUl li{
	height:40px;
	line-height:40px;
	border-bottom: 1px solid #ddd;
    width: 600px;
}
.kwdUl li span{
	display:inline-block;
	width:107px;
}
#pageDiv{
	text-align:center;
	width:600px;
	margin-top:20px;
}
#pageDiv a{
	text-decoration:none;
	color: #000;
    margin-left: 5px;
}
</style>
<script type="text/javascript">
	 $(function(){
		var pageSize = 10;
		var totalPageNum = $("input[name=totalPageNum]").val();
		var curPage = $("input[name=curPage]").val();
		var wrap = $(".listWrap")[0];
		setPage(curPage, totalPageNum, pageSize, wrap);
		adapBodySize();
		$(window).resize(function(){
			adapBodySize();
		});
	}); 
	 
	 function adapBodySize(){
		 var wrap_height = $(window).height();
		$(".rightWrap").css({"height" : wrap_height});
	 }
	 function getDataByPage(clickPage){
		 var url = "/SecureInspect/keyword/keyword0/" + clickPage;
		 $.ajax({
			async : false,
			cache : false,
			type : "GET",
			url : url,
			dataType : "html",
			success : function(data) {
				$(".listWrap").html(data);
				var pageSize = 10;
				var totalPageNum = $("input[name=totalPageNum]").val();
				var curPage = $("input[name=curPage]").val();
				var wrap = $(".listWrap")[0];
				setPage(curPage, totalPageNum, pageSize, wrap);
			},				
			error : function(msg) {
				console.log(msg);
			}
		 });
	 }
	function callbackInfo(data){
		var t = eval('(' + data + ')');
		var key = t.value;
		var rank = t.rank;
		if(t.flag == "ok"){
			getDataByPage(1);
		}
	}
</script>
<body>
	<div class="rightWrap">
		<a id="addKwd">新增关键字</a>
		<iframe name="iframe" style="display:none;"></iframe>
		<form:form modelAttribute="keyword" id="keyForm" action="/SecureInspect/keyword/addKey" target="iframe">
			
			<ul class="addUl">
				<li>
					<span>关键字：</span><input type="text" name="value">
				</li>
				<li>
					<span>密&nbsp;&nbsp;级：</span><input type="text" name="rank">
				</li>
				<li>
					<input type="submit" value="增加"><input type="reset" value="取消">
				</li>
			</ul>
		</form:form>
		
		<a class="listBtn">关键字列表</a>
		<div class="listWrap">
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
		</div>
	</div>
</body>
</html>