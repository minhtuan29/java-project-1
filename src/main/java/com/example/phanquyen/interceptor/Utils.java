package com.example.phanquyen.interceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Utils {

    public static boolean containsParamValue(HttpServletRequest request, Predicate<String> predicate){
        var paramKeys = request.getParameterMap().keySet();
        var paramValues = paramKeys.parallelStream().flatMap(i -> Arrays.stream(request.getParameterValues(i)));
        if(paramValues.anyMatch(predicate)){
            return true;
        }
        return false;
    }


    public static boolean containsParamValue(HttpServletRequest request, List<Predicate<String>> predicates){
        var paramKeys = request.getParameterMap().keySet();
        var paramValues = paramKeys.parallelStream().flatMap(i -> Arrays.stream(request.getParameterValues(i)));
        for(var predicate : predicates){
            if( paramValues.anyMatch(predicate)){
                return true;
            }
        }
        return false;
    }

}
