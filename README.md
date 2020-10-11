# Retrofit-Sugar

The purpose of this project is to add some useful features for okhttp/retrofit rest cient.

## RetryInterceptor

it's a universal interceptor for retrying API calls.

### Usage

<pre>
RetryInterceptorBuilder retryInterceptorBuilder = RetryInterceptor
            .retry()
            .untilTimes(3)
            .each(100, MILLISECONDS)
            .withListener(retryCountListener)
            .ifConditionOccurs(Code.equalsTo(200)).negate()
            .ifExceptionThrown(ArithmeticException.class)
            .build();
            
OkHttpClient client = new OkHttpClient.Builder().addInterceptor().build();
</pre>

In this particular case any API request made by `client` will be retryed up to 3 times each 100 ms while response code is not 200 and no exception is thrown.
