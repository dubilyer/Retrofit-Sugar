package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.interceptor;

import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import okhttp3.Response;
import org.dubilyer.retrofit_tools.interceptor.retry_interceptor.listener.RetryRequestListener;

import java.util.concurrent.TimeUnit;

public class RetryInterceptorBuilder {
    private final RetryListener retryListener = new RetryRequestListener();

    RetryerBuilder<Response> retryerBuilder = RetryerBuilder
            .<Response>newBuilder()
            .withRetryListener(retryListener);


    public RetryInterceptorBuilder ifConditionOccurs(java.util.function.Predicate<Response> predicate){
        retryerBuilder.retryIfResult(predicate::test);
        return this;
    }

    public RetryInterceptorBuilder ifExceptionThrown(){
        retryerBuilder.retryIfException();
        return this;
    }

    public RetryInterceptorBuilder ifExceptionThrown(Class<? extends Throwable> clazz){
        retryerBuilder.retryIfExceptionOfType(clazz);
        return this;
    }

    public RetryInterceptorBuilder untilTimes(int times){
        retryerBuilder.withStopStrategy(StopStrategies.stopAfterAttempt(times));
        return this;
    }

    public RetryInterceptorBuilder until(long duration, TimeUnit unit){
        retryerBuilder.withStopStrategy(StopStrategies.stopAfterDelay(duration, unit));
        return this;
    }

    public RetryInterceptorBuilder each(long duration, TimeUnit unit){
        retryerBuilder.withWaitStrategy(WaitStrategies.fixedWait(duration, unit));
        return this;
    }

    public RetryInterceptorBuilder withListener(RetryListener retryListener) {
        retryerBuilder.withRetryListener(retryListener);
        return this;
    }

    public RetryInterceptor build(){
        return new RetryInterceptor(
                retryerBuilder.build()
        );
    }

}