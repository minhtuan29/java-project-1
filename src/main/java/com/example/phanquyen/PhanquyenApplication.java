package com.example.phanquyen;

import com.example.phanquyen.interceptor.HackerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class PhanquyenApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhanquyenApplication.class, args);
        openHomePage();
    }

    static void openHomePage()  {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("rundll32 url.dll,FileProtocolHandler http://localhost:8080/admin/list-employee");
        }catch (Exception e){

        }
    }


}
