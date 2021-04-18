package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ActivityController extends HttpServlet {


    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入市场活动控制器");
        String path = request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){

            //获取用户信息数组
            getUserList(request,response);

        } else if("/workbench/activity/save.do".equals(path)){

            //添加市场活动都数据库
            save(request,response);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的添加操作");

        //取出异步请求对象携带的参数
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();  //创建时间为当前的系统时间
        User user = (User)request.getSession().getAttribute("user"); //创建人为当前登录的用户（从session中取user，从user中取name）
        String createBy = user.getName();

        //将信息封装到一个Activity对象中
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);



        //通过User业务层获取用户信息列表（jdk动态代理）
        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        Boolean success = as.save(activity);


        //把success解析成json串，并写入响应体
        PrintJson.printJsonFlag(response,success);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得所有的用户信息");

        //通过User业务层获取用户信息列表（jdk动态代理）
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        //把uList解析成json数组，并写入响应体
        PrintJson.printJsonObj(response,uList);
    }

}
