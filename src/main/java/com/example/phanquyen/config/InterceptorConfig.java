package com.example.phanquyen.config;

import com.example.phanquyen.interceptor.AdminInterceptor;
import com.example.phanquyen.interceptor.HackerInterceptor;
import com.example.phanquyen.interceptor.LinuxStatementInterceptor;
import com.example.phanquyen.interceptor.SqlInjectionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    AdminInterceptor adminInterceptor;

    @Autowired
    LinuxStatementInterceptor linuxStatementInterceptor;

    @Autowired
    HackerInterceptor hackerInterceptor;

    @Autowired
    SqlInjectionInterceptor sqlInjectionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(hackerInterceptor)
                        .addPathPatterns("/**");

        registry.addInterceptor(linuxStatementInterceptor)
                        .addPathPatterns("/**");

        registry.addInterceptor(sqlInjectionInterceptor)
                .addPathPatterns("/**");


//        registry.addInterceptor(adminInterceptor)
//                .addPathPatterns("/admin/**")
//                .excludePathPatterns("admin/logout");

    }


}




















