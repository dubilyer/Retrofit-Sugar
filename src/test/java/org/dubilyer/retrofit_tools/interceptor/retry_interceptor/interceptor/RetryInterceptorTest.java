package org.dubilyer.retrofit_tools.interceptor.retry_interceptor.interceptor;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.RetryListener;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.dubilyer.retrofit_tools.interceptor.retry_interceptor.matcher.Code;
import org.dubilyer.retrofit_tools.interceptor.retry_interceptor.matcher.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetryInterceptorTest {
    int retries = 0;

    RetryListener retryCountListener = new RetryListener() {
        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            retries++;
        }
    };

    RetryInterceptorBuilder retryInterceptorBuilder = RetryInterceptor
            .retry()
            .untilTimes(3)
            .each(100, MILLISECONDS)
            .withListener(retryCountListener);


    Response provideResponseWithCode(int code) {
        Request request = new Request.Builder().url("http://a.com").get().build();
        return new Response.Builder().code(code).request(request).protocol(Protocol.HTTP_1_1).message("test").build();
    }

    Response provideCode200On3rdRetry(){
        return provideResponseWithCode((retries==2)?200:300);
    }


    static Stream<Predicate<Response>> predicateProvider() {
        return Stream.of(
                Code.equalsTo(300),
                Code.equalsTo(400).negate(),
                Code.lessThan(400),
                Code.moreThan(200),
                Message.contains("es"),
                Message.equalsTo("test")
        );
    }

    @BeforeEach
    void resetCount() {
        retries = 0;
    }

    @ParameterizedTest
    @MethodSource("predicateProvider")
    void retryIfConditionOccurs(Predicate<Response> condition) {
        RetryInterceptor retryInterceptor = retryInterceptorBuilder.ifConditionOccurs(condition).build();
        assertThrows(
                RetryException.class,
                () -> retryInterceptor.retryer.call(() -> provideResponseWithCode(300))
        );
        assertEquals(3, retries);
    }

    @Test
    void successOn3rdTime() throws ExecutionException, RetryException {
        RetryInterceptor retryInterceptor = retryInterceptorBuilder.ifConditionOccurs(Code.equalsTo(200).negate()).build();
        Response response = retryInterceptor.retryer.call(this::provideCode200On3rdRetry);
        assertEquals(200, response.code());
        assertEquals(3, retries);
    }


    @Test
    void retryIfExceptionThrown() {
        RetryInterceptor retryInterceptor = retryInterceptorBuilder.ifExceptionThrown().build();
        assertThrows(
                RetryException.class,
                () -> retryInterceptor.retryer.call(() -> {
                    throw new RuntimeException("a");
                })
        );
        assertEquals(3, retries);
    }

    @Test
    void retryIfSpecificExceptionThrown() {
        RetryInterceptor retryInterceptor = retryInterceptorBuilder.ifExceptionThrown(ArithmeticException.class).build();
        assertThrows(
                RetryException.class,
                () -> retryInterceptor.retryer.call(() -> {
                    throw new ArithmeticException("a");
                })
        );
        assertEquals(3, retries);
    }
}
