package com.simplemented.okdelay;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

public class DelayInterceptorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeDuration() throws Exception {
        new DelayInterceptor(-1L, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullTimeUnit() throws Exception {
        new DelayInterceptor(1L, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullDelayProvider() throws Exception {
        new DelayInterceptor(null);
    }

    @Test()
    public void testConstructor_zeroDuration() throws Exception {
        final DelayInterceptor delayInterceptor = new DelayInterceptor(0L, TimeUnit.SECONDS);

        assertNotNull(delayInterceptor);
    }
}