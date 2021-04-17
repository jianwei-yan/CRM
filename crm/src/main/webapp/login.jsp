<%--
Created by IntelliJ IDEA.
User: 玖仟柒
Date: 2021/4/17
Time: 16:08
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script>
		$(function () {

			//页面加载完毕后，让用户的文本框内容清空、自动获得焦点
			$("#loginAct").val("");
			$("#loginAct").focus();

			/*执行登录操作*/
			//1、点击登录按钮执行登录操作（为登录按钮绑定事件）
			$("#submitBtn").click(function () {
				login();
			})
			//2、敲键盘的回车键执行登录操作（为当前的登录页窗口绑定敲击键盘事件）
			$(window).keydown(function (event) {
				//如果敲击的是回车键，触发事件
				if(event.keyCode == 13){
					login();
				}
			})
		})

		//登录操作函数，要写在$(function (){})的外边
		function login() {
			//alert("登录操作");
			//验证账号密码不能为空
			//1、取得用户输入的账号和密码（使用$.trim()将输入的空格去掉）
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			//2、验证用户是否输入了账号密码
			if(loginAct=="" || loginPwd == ""){
				$("#msg").html("账号密码不能为空");
				//如果账号密码为空，则强制终止该登录方法
				return false;
			}
			
			//3、去后台验证登录相关的操作（ajax发送异步请求，局部刷新）
			$.ajax({
				url:"setting/user/login.do",
				data:{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					/*
					* 服务器端返回的处理结果data: {"sucess":true/false,
					* 						     "msg":"登录信息出错的具体地方"}
					* */
					if(data.success){

						//登录成功，跳转到工作台的欢迎页
						window.location.href = "workbench/index.html";
					}else{

						//登录失败，显示失败具体的原因
						$("#msg").html(data.msg);

					}
				}
			})

		}

	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>

					<%--注意：
							将按钮写在form表单中，默认的行为就是提交表单
							一定要将按钮的类型设置为button，所触发的行为应当由我们写的js代码来决定
					--%>
					<button type="button"  id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>