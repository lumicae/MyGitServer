<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">  
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">  
<META HTTP-EQUIV="expires" CONTENT="0">
<title>账户管理</title>
<script type="text/javascript" src="/SecureInspect/js/pageTool.js"></script>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>

<style>
a, span, input{
	font-family:microsoft yahei;
	margin:0;
	padding:0;
	display:inline-block;
}
ul,li{
	list-style:none;
	margin:0;
	padding:0;
}
.username{
	display: block;
    text-decoration: none;
    height: 50px;
    line-height: 50px;
    font-size: 20px;
    background-color: #eee;
    width: 300px;
}
.mdfPwdBtn, .userListBtn{
    font-size: 18px;
    height: 30px;
    line-height: 30px;
    margin-top: 20px;
    background-color: #ddd;
    width: 150px;
    text-align: center;
    border-radius: 10px;
}
.modifyPwd, .userlist{
    margin-top: 20px;
    font-size: 18px;
    width:500px;
}
.modifyPwd li{
    height: 60px;
    line-height: 60px;
}
.modifyPwd li input{
	height: 30px;
    line-height: 30px;
    font-size:18px;
    margin-left:20px;
}
.modifyPwd li span{
	width:100px;
}
.userListBtn, .userList{
	font-size:18px;
}
.userlist li{
	height:50px;
} 
.liUser span:last-child input{
	font-size: 18px;
}
.userlist li span{
	width:70px;
	text-align:center;
}
#pageDiv a{
	text-decoration:none;
	color: #000;
    margin-left: 5px;
}
</style>
<script type="text/javascript">
	function updatePwd(){
		var obj = {};
		obj.oldPwd = $("#oldPwd").val();
		obj.newPwd = $("#newPwd").val();
		obj.confirPwd = $("#confirPwd").val();
		var url = "/SecureInspect/account/updatePwd"
		$.ajax({
			cache:false,
			sync:false,
			type:"post",
			data: {"obj":JSON.stringify(obj)},
			url:url,
			dataType:"text",
			success:function(data){
				if(data == "ok"){
					alert("密码更新成功");
				}
				else if(data == "unlogin"){
					alert("未登录");
				}
				else{
					alert("密码保存失败");
				}
			},
			error:function(e){
				console.log(e);
			}
		});
	}
</script>
</head>
<body>
	<a class="username">当前用户为：${username}</a>
	<a class="mdfPwdBtn">修改密码</a>
	<ul class="modifyPwd">
		<li>
			<span>原密码：</span><input type="password" id="oldPwd">
		</li>
		<li>
			<span>新密码：</span><input type="password" id="newPwd">
		</li>
		<li>
			<span>确认密码：</span><input type="password" id="confirPwd">
		</li>
		<li class="liBtn">
			<input type="button" value="修改" onclick="updatePwd()"><input type="button" value="取消">
		</li>
	</ul>
</body>
</html>