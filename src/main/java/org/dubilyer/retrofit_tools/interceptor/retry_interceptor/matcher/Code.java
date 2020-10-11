package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.matcher;

import java.util.function.Predicate;
import okhttp3.Response;

public class Code {
    private Code() {
    }

    public static Predicate<Response> equalsTo(int code){
        return r -> r.code()==code;
    }

    public static Predicate<Response> moreThan(int code){
        return r -> r.code()>code;
    }

    public static Predicate<Response> lessThan(int code){
        return r -> r.code()<code;
    }
}
