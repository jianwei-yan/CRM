package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    //设计一个功能模板
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入用户控制器");
        String path = request.getServletPath();

        if("/setting/user/login.do".equals(path)){
            //登录验证
            login(request,response);

        } else if("/setting/user/xxx.do".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到验证登录操作");
        //接受参数（浏览器发起的异步请求登录携带的参数）
        String loginAct = request.getParameter("loginAct"); //账号
        String loginPwd = request.getParameter("loginPwd"); //密码
        loginPwd = MD5Util.getMD5(loginPwd);  //将密码的明文形式转换为MD5密文形式
        String ip = request.getRemoteAddr();  //浏览器端的ip地址

        //未来的开发统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            //调用业务层的login()方法
            User user = us.login(loginAct,loginPwd,ip);

            request.getSession().setAttribute("user",user);  //将登录信息存放在session域中，供以后使用（数据共享）

            //如果程序到这，说明业务层没有抛出异常，登录成功

            /*
            * 给前端返回的json为：{"success":true}
            * */
            //将服务器处理的结果转换为json串，并写入响应体
            PrintJson.printJsonFlag(response,true);


        } catch (Exception e){
            e.printStackTrace();

            //如果程序到这，说明业务层抛出异常，登录失败，
            /*
             * 给前端返回的json为：{"success":false,"msg":？}
             * */
            String msg = e.getMessage();  //获取登录失败原因msg(异常抛出信息)
            //将success和msg信息打包成map集合
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);

            //将服务器处理的结果转换为json串，并写入响应体
            PrintJson.printJsonObj(response,map);
        }
    }
}
