package com.example.phanquyen.interceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Predicate;

public class Utils {

    public static boolean containsParamValue(HttpServletRequest request, Predicate<String> predicate){
        var paramKeys = request.getParameterMap().keySet();
        for(var paramKey : paramKeys){
            var paramValue = request.getParameterValues(paramKey);
            for(var ele : paramValue){
                if(predicate.test(ele)){
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean containsParamValue(HttpServletRequest request, List<Predicate<String>> predicates){
        var paramKeys = request.getParameterMap().keySet();
        for(var paramKey : paramKeys){
            var paramValue = request.getParameterValues(paramKey);
            for(var ele : paramValue){
                for(var predicate : predicates){
                    if(predicate.test(ele)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
