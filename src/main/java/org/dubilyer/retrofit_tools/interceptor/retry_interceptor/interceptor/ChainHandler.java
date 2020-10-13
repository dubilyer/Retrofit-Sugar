package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.interceptor;

import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.Callable;

import static okhttp3.Interceptor.Chain;

class ChainHandler implements Callable<Response> {
    private final Chain chain;

    public ChainHandler(Chain chain) {
        this.chain = chain;
    }

    @Override
    public Response call() throws Exception {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
