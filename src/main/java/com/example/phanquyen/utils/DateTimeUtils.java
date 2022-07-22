package com.example.phanquyen.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateTimeUtils {

    public static String getDateStrInFormat(String form){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(form));
    }

}
