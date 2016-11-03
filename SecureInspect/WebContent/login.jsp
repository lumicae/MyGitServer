<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页-登录</title>
<script type="text/javascript" src="/SecureInspect/js/jQuery/jquery-1.7.2.js"></script>
</head>
<style>
body,div{
	margin:0;
	padding:0;
}

.background{
	overflow:hidden;
	/* background:-webkit-gradient(radial,850 350,0,850 350,400,from(#C2E1F3),to(#3f9ed4)); */
	background-color:#3f9ed4;
}

.content{
	height:300px;
	width:700px;
	margin:auto;
	margin-top:10px;
	background-color: #3D98C5;
	border-radius:5px 5px 0px 0px;
}
.titleDiv{
	width:calc(100% - 5px);
	height:48px;
	background-color: #2990CB;
	border:1px solid #D1E8F6;
	border-radius:10px 10px 0 0;
	margin:1px 2px 0px 2px;
	color:#fff;
	font-family:Microsoft Yahei;
	font-size:20px;
	text-align:center;
	line-height:48px;
}
.contentDiv{
	width:calc(100% - 4px);
	height:250px;
	margin:0px 2px 0px 2px;
	FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#D1E6F7,endColorStr=#BBE1F4); /*IE 6 7 8*/ 

	background: -ms-linear-gradient(top, #D1E6F7,  #BBE1F4);        /* IE 10 */

	background:-moz-linear-gradient(top,#D1E6F7,#BBE1F4);/*火狐*/ 

	background:-webkit-gradient(linear, 0% 0%, 0% 100%,from(#D1E6F7), to(#BBE1F4));/*谷歌*/ 

	background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#D1E6F7), to(#BBE1F4));      /* Safari 4-5, Chrome 1-9*/

	background: -webkit-linear-gradient(top, #D1E6F7, #BBE1F4);   /*Safari5.1 Chrome 10+*/

	background: -o-linear-gradient(top, #D1E6F7, #BBE1F4);  /*Opera 11.10+*/
}
.left{
	float:left;
	width:calc(50% - 1px);
	height:100%;
}
.left img{
    margin:55px auto;
    width: 104px;
    display: block;
    
}
.line{
	float:left;
	width:2px;
	height: calc(100% - 40px);
	margin:20px 0 20px 0;
	background-image:-webkit-gradient(linear,0% 0%, 100% 0%, from(#BED8E7), to(#BED8E7),color-stop(20%,#8BACBD)); 
}
.right{
	float:right;
	width: calc(50% - 1px);
	height:100%;
	font-size:18px;
	font-family:Microsoft Yahei;
}
.right span{
	color:#315A70;
	font-weight:bold;
	width:50px;
	padding-left:15px;
}
.right div{
	height:50px;
	width:100%;
}
.username{
	margin-top:55px;
}
.right input{
	height:25px;
	font-size:18px;
	font-family:Microsoft Yahei;
}
.right #checkCode{
	width:calc(25% - 1px);
}
.right .checkImg{
	width:calc(25% - 1px);
	margin-left: 5px;
    height: 32px;
    vertical-align: middle;
}

.btnDiv input{
	height:30px;
}
.btnDiv .submit{
	margin-left:85px;
}
.btnDiv .cancel{
	margin-left:20px;
}
.logoDiv{
	height:70px;
	width:520px;
	color:#fff;
	font-size:36px;
	font-family:microsoft yahei;
	margin:auto;
	margin-top:200px;
}
.logoDiv span{
    height: 70px;
    line-height: 70px;
    display: inline-block;
    overflow: hidden;
    padding-left: 20px;
}
.toggleBtn{
	cursor:pointer;
	color:#315A70;
	text-align:right;
	width:calc(100% - 10px) !important;
	text-decoration:underline;
	margin-top:-25px;
}
#registerForm, .loginBtn{
	display:none;
}
</style>
<script type="text/javascript">
	$(function(){
		if(window != top){
			top.location.href = location.href;
		}
		var height = $(window).height();
		var width = $(window).width();
		$(".background").css({"height":height,"width":width});
	});
	function showRegister(){
		$("#loginForm").hide();
		$("#registerForm").show();
		$(".registerBtn").hide();
		$(".loginBtn").show();
	}
	function showLogin(){
		$("#loginForm").show();
		$("#registerForm").hide();
		$(".registerBtn").show();
		$(".loginBtn").hide();
	}
	function callbackInfo(data){
		var obj = eval('(' + data + ')');
		if(obj.flag == "failed"){
			alert("注册失败");
		}
		else{
			var form = $("#loginForm");
			$("#loginForm #username").val(obj.username);
			$("#loginForm #password").val(obj.password);
			form.submit();
		}
	}
	function loginCallback(data){
		var obj = eval('(' + data + ')');
		if(obj.flag == "failed"){
			alert("登录失败，信息填写错误");
		}
	}
	
	function userLogin(){
		var form = $("#loginForm")[0];
		var username = $(form).find("#username").val();
		var password = $(form).find("#password").val();
		var checkCode = $(form).find("#checkCode").val();
		if(username == "" || password == "" || checkCode == ""){
			alert("信息不完整");
			return;
		}
		else{
			form.action = "/SecureInspect/account/checkLoginInfo";
			form.submit();
		}
	}
	function userRegister(){
		var form = $("#registerForm")[0];
		var username = $(form).find("#username").val();
		var password = $(form).find("#password").val();
		if(username == "" || password == ""){
			alert("信息不完整");
			return;
		}
		else{
			form.action = "/SecureInspect/account/checkSaveRegisterInfo";
			form.submit();
		}
	}
	function cancelLogin(){
		$("#loginForm").find("#username").val("");
		$("#loginForm").find("#password").val("");
	}
	function cancelRegister(){
		$("#registerForm").find("#username").val("");
		$("#registerForm").find("#password").val("");
	}
	function changeCheckCode(){
		 var img = document.getElementById("checkImg");
		 img.src= "/SecureInspect/account/checkNumberShow?timeStamp="+new Date().getTime();
	}
	function keyLogin(){
		if (event.keyCode==13){
			userLogin();
		}
	}
</script>
<body onkeydown="keyLogin()">
	<div class="background">
		<div class="logoDiv"><img src="/SecureInspect/images/comLogo.gif"><span>中国科学院信息工程研究所</span></div>
		<div class="loginDiv content">
			<div class="titleDiv">Web保密检查系统V1.0</div>
			<div class="contentDiv">
				<div class="left"><img src="/SecureInspect/images/check.png"></div>
				<div class="line"></div>
				<div class="right">
				<iframe name="loginIframe" style="display:none;"></iframe>
				<form id="loginForm" method="post" target="loginIframe">
					<div class="username"><span class="username_span">用户名：</span><input type="text" id="username" name="username" autocomplete="off"></div>
					<div class="password"><span class="password_span">密&nbsp;&nbsp;&nbsp;码：</span><input type="password" id="password" name="password"autocomplete="off"></div>
					<div class="checkCode"><span class="checkCode_span">验证码：</span><input type="text" id="checkCode" name="checkCode" autocomplete="off"><img id="checkImg" title="换一张" src="/SecureInspect/account/checkNumberShow" class="checkImg" onclick="changeCheckCode();"></div>
					<div class="btnDiv"><input class="submit" type="button" value="登录" onclick="userLogin()"><input class="cancel" type="button" value="取消" onclick="cancelLogin()"></div>
				</form>
				<iframe name="registerIframe" style="display:none"></iframe>
				<form:form id="registerForm" method="post" target="registerIframe">
					<div class="username"><span class="username_span">用户名：</span><input type="text" id="username" name="username" autocomplete="off"></div>
					<div class="password"><span class="password_span">密&nbsp;&nbsp;&nbsp;码：</span><input type="password" id="password" name="password" autocomplete="off"></div>
					<div class="btnDiv"><input class="submit" type="button" value="注册" onclick="userRegister()"><input class="cancel" type="button" value="取消" onclick="cancelRegister()"></div>
				</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>