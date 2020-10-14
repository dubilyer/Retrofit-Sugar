package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.interceptor;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import okhttp3.Response;

import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

class RetryRequestListener implements RetryListener {
    Logger logger = Logger.getLogger(RetryRequestListener.class.getSimpleName());

    @Override
    public <V> void onRetry(Attempt<V> attempt) {
        StringBuilder log = new StringBuilder(String.format("Retry #%d. Result: ", attempt.getAttemptNumber()));
        if (attempt.hasResult()) {
            Response response = (Response) attempt.getResult();
            if (response.body() != null) {
                response.close();
            }
            log.append(attempt.getResult());
        } else if (attempt.hasException() && logger.isLoggable(WARNING)) {
            log.append("Exception ").append(attempt.getExceptionCause().getMessage());
        }
        if (logger.isLoggable(WARNING)) {
            logger.warning(log.toString());
        }
    }
}