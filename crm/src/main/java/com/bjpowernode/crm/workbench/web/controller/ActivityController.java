package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        } else if ("/workbench/activity/pageList.do".equals(path)){

            //查询所有的市场活动信息
            pageList(request,response);
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动查询操作（结合条件查询、分页查询），展现市场活动列表");

        //取出发送的参数信息
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);  //每页展现的记录数
        int skipCount = (pageNo-1)*pageSize;          //略过的记录数，limit()的第一个参数

        //将信息封装到一个map对象中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        //通过ActivityService业务层查询市场活动信息（jdk动态代理）
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVO<Activity> activityVo= as.pageList(map);   //返回值为VO


        //把activityVo解析成json串，并写入响应体
        //activityVo----->>{"total":100,"dataList":[{市场活动1},{},{}...]}
        PrintJson.printJsonObj(response,activityVo);

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



        //通过UserService业务层获取用户信息列表（jdk动态代理）
        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        Boolean success = as.save(activity);


        //把success解析成json串，并写入响应体
        PrintJson.printJsonFlag(response,success);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得所有的用户信息");

        //通过UserService业务层获取用户信息列表（jdk动态代理）
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        //把uList解析成json数组，并写入响应体
        PrintJson.printJsonObj(response,uList);
    }

}
