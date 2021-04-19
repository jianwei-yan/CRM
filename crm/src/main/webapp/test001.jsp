<%--
  Created by IntelliJ IDEA.
  User: 玖仟柒
  Date: 2021/4/17
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script>


        $.ajax({
            url:"",
            data:{},
            type:"",
            dataType:"json",
            success:function () {

            }
        });

        //创建时间为当前的系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人为当前登录的用户（从session中取user，从user中取name）
        User user = (User)request.getSession().getAttribute("user");
        String createBy = user.getName();

    </script>
</head>
<body>

</body>
</html>
