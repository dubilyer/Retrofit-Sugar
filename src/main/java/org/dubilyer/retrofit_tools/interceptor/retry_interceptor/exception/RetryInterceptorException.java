package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.exception;

public class RetryInterceptorException extends RuntimeException {

    public RetryInterceptorException(Exception e) {
        super(e);
    }
}
