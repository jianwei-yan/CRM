package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入验证是否登录过的过滤器（验证session）");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();

        if ("/login.jsp".equals(path) || "/setting/user/login.do".equals(path)){

            //把登录验证页自动放行
            chain.doFilter(req,resp);
        }else{

            //对于其他的文件进行验证
            //需要通过HttpSession获取session，存放的是user
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if(user != null){

                //登录过
                chain.doFilter(req,resp);
            } else{

                //重定向到登录页
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }

        }

    }
}
