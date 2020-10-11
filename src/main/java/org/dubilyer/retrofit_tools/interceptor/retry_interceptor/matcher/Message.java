package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.matcher;

import java.util.function.Predicate;
import okhttp3.Response;

public class Message {
    private Message() {
    }

    public static Predicate<Response> contains(String s){
        return x -> x.message().contains(s);
    }

    public static Predicate<Response> equalsTo(String s){
        return x -> x.message().equals(s);
    }
}
