<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
</head>
<style type="text/css">
	#instruct{
	    background-color: #ddd;
	    text-align: center;
	    margin:10px auto;
	    width: 150px;
	    display: inline-block;
	    height: 30px;
	    line-height: 30px;
	    border-radius: 10px;
}
	}
	div, a, p{
		font-family:microsoft yahei;
		font-size:20px;
		margin:auto;
		display:block;
	}
	p{
		width:85%;
		line-height:40px;
	}
	.textWrap{
		width:100%;
		overflow:auto;
	}
	.instructDiv{
		width:calc(100% - 10px);
	}
</style>
<script type="text/javascript">
	$(function(){
		adaptBodySize();
		$(window).resize(function(){
			adaptBodySize();
		});
	});
	function adaptBodySize(){
		var height = $(window).height();
		$(".textWrap").height(height-20);
	}
</script>
<body>
		
	<div class="textWrap">
		<div class="instructDiv">
			<a id="instruct">使用说明</a>
			<p>1.搜索功能分为两种模式:简洁搜索和高级搜索。</p>
			<p>2.简洁搜索分为两类：关键字和网址。示例：绝密；www.abc.com。</p>
			<p>3.高级搜索的条件包含:时间、网站名称、资源名称（网页、图片、附件的title）、
			搜索结果每页显示的条数以及涉密鉴定结果。
			</p>
			<p>4.高级搜索中的时间选项：点击日历图标，弹出时间选择控件。在控件中选择时间后，结果自动填入时间文本框。</p>
			<p>5.“（关键字OR网址URL）AND 时间 AND 资源名称 AND 网站名称 AND 每页条数 AND 涉密鉴定” 作为高级搜索的条件。</p>
			<p>6.点击”展开“按钮，展示该网站内所有含有涉密关键字的网络资源：网页正文、图片、附件等。</p>
			<p>7.”处理“按钮返回的结果分三种：是（涉密）、否（非涉密）、可疑（不确定）；对应标注颜色：红，黑，蓝。</p>
			<p>8.每一资源的初始涉密值为”否“，处理操作的结果影响是否涉密和涉密数的变化。当资源被判定为涉密时，涉密鉴定的值为”是“，
			涉密数加1，该资源条目字体显示为红色；当判定为不涉密时，涉密鉴定值为否，涉密数不变，该资源条目字体显示为黑色；
			当不确定是否涉密时，涉密鉴定值为可疑，涉密数不变，该资源条目字体显示为蓝色。
			</p>
			<p>9.当用户需要新增检查任务时，在任务管理栏目中填写新任务信息并点击“增加”按钮。</p>
			<p>10.新建任务填写实例：网站URL:http://www.baidu.com；网站名称：百度。URL中一定要填写协议http或https，避免任务执行结果不准确。</p>
		</div>
	</div>
</body>
</html>