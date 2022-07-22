package com.example.phanquyen.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@Component
public class HackerInterceptor implements HandlerInterceptor {

    public static Set<String> hackerIPs = new HashSet<>();

    static int c = 0;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if( hackerIPs.contains(request.getRemoteAddr())){
            System.out.println("=================  " + c++ + "  ==================");
            System.out.println("Hacker đang quay lại, đã chặn anh ấy !!!");
            System.out.println("anh ấy dùng : ");
            System.out.println(request.getHeader("sec-ch-ua"));
            response.sendRedirect("disconnect");
            return false;
        }
        return true;
    }
}
