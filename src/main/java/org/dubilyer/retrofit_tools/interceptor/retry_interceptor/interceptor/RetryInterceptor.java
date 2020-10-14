package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.interceptor;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.dubilyer.retrofit_tools.interceptor.retry_interceptor.exception.RetryInterceptorException;

import java.util.concurrent.ExecutionException;

public final class RetryInterceptor implements Interceptor {
    final Retryer<Response> retryer;

    RetryInterceptor(Retryer<Response> retryer) {
        this.retryer = retryer;
    }

    public static RetryInterceptorBuilder retry(){
        return new RetryInterceptorBuilder();
    }

    @Override
    public Response intercept(Chain chain) {
        try {
            return retryer.call(new ChainHandler(chain));
        } catch (ExecutionException | RetryException e) {
            throw new RetryInterceptorException(e);
        }
    }

}