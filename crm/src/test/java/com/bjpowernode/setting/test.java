package com.bjpowernode.setting;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {

        //1、验证失效时间
        /*//失效时间
        String expireTime = "2020-10-10 10:10:10";
        //获取当前系统时间
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);   //>0:有效  <0失效*/


        //2、验证锁定状态
       /* String lockState = "0";
        if ("0".equals(lockState)){
            System.out.println("账号已经锁定");
        }*/

       //3、验证有效IP地址
        String ip = "192.168.1.1";  //浏览器ip
        String allowIps = "192.168.1.1,192.168.1.2";  //允许访问的ip
        if (allowIps.contains(ip)){
            System.out.println("ip有效");
        }


        //4、测试MD5 明文--->密文
        String pw = "y123456";  //明文
        pw = MD5Util.getMD5(pw); //密文
        System.out.println(pw);





    }
}
