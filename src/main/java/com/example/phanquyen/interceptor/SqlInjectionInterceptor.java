package com.example.phanquyen.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.List;
import java.util.function.Predicate;

import static com.example.phanquyen.interceptor.Utils.*;
@Component
public class SqlInjectionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<Predicate<String>> predicates = List.of(e -> e.contains("select") && e.contains("from")
                                                    ,e -> e.contains("or") && e.contains("'1' = '1'")
                                                    ,e -> e.contains("'or 1 = 1"));

        if ( containsParamValue(request, predicates) ){
            HackerInterceptor.hackerIPs.add(request.getRemoteAddr());
            response.sendRedirect("/disconnect");
            return false;
        }
        return true;
    }
}
