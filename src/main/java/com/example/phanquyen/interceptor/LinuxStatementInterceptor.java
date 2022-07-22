package com.example.phanquyen.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.phanquyen.interceptor.Utils.*;


@Component
public class LinuxStatementInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ( containsParamValue(request, e -> e.equals("shutdown -r now")) ){
            HackerInterceptor.hackerIPs.add(request.getRemoteAddr());
            response.sendRedirect("disconnect");
            return false;
        }
        return true;

    }
}
