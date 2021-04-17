package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {

    //作为Service成员变量，用于调用Dao层实现与数据库交互
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        System.out.println("用户输入的账号："+loginAct);
        System.out.println("用户输入的密码："+loginPwd);
        System.out.println("用户输入的ip："+ip);

        //通过UserDao对象，调用dao层与数据库交互，返回查询的结果user
        User user = userDao.login(map);

        System.out.println("数据库中的账号："+user.getLoginAct());
        System.out.println("数据库中的密码："+user.getLoginPwd());


        //验证账号密码
        if (user == null){
            throw new LoginException("账号密码错误");
        }

        /*
        *如果程序执行到这里，说明账号密码正确，需要继续验证其他的信息
        * 账号失效时间、锁定状态、ip地址
        * */
        //验证失效时间
        String expireTime = user.getExpireTime();  //失效时间
        String currentTime = DateTimeUtil.getSysTime(); //当前系统时间
        if (expireTime.compareTo(currentTime)<0){
            //>0:有效  <0失效
            throw new LoginException("账号已失效");
        }

        //验证锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //验证有效IP地址
        String ips = user.getAllowIps();  //允许访问的ip
        if (!(ips.contains(ip))){
            throw new LoginException("IP地址受限");
        }

        //如果程序执行到这里，说明用户的各项信息均正确
        return user;
    }
}
