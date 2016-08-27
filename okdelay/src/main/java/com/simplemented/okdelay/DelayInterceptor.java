package com.simplemented.okdelay;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DelayInterceptor implements Interceptor {

    public interface DelayProvider {
        /**
         * Provides delay duration in milliseconds.
         *
         * @return delay in milliseconds
         */
        @Nonnegative
        long getDelay();
    }

    @Nonnull
    private final DelayProvider delayProvider;

    /**
     * Constructs fixed time {@link DelayInterceptor}.
     *
     * @param duration duration of delay
     * @param timeUnit unit of delay
     */
    public DelayInterceptor(@Nonnegative final long duration, @Nonnull final TimeUnit timeUnit) {
        if (duration < 0) throw new IllegalArgumentException("duration cannot be negative value.");
        if (timeUnit == null) throw new NullPointerException("timeUnit cannot be null.");
        this.delayProvider = () -> timeUnit.toMillis(duration);
    }

    /**
     * Constructs {@link DelayInterceptor} where delay duration is not fixed. For example, using
     * {@link SimpleDelayProvider} you may change delay during app running by using one of its
     * setters.
     *
     * @param delayProvider delay duration in milliseconds provider. Negative values provided by
     *                      this provider will cause no delay.
     */
    public DelayInterceptor(@Nonnull final DelayProvider delayProvider) {
        if (delayProvider == null) throw new NullPointerException("delayProvider cannot be null.");
        this.delayProvider = delayProvider;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final long delay = delayProvider.getDelay();
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        return chain.proceed(chain.request());
    }
}
