package com.simplemented.okdelay;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

public class SimpleDelayProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeDuration() throws Exception {
        new SimpleDelayProvider(-1L, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullTimeUnit() throws Exception {
        new SimpleDelayProvider(1L, null);
    }

    @Test
    public void testConstructor_zeroDuration() throws Exception {
        final SimpleDelayProvider delayProvider = new SimpleDelayProvider(0L, TimeUnit.SECONDS);

        assertNotNull(delayProvider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDelay_negativeDuration() throws Exception {
        new SimpleDelayProvider(0L, TimeUnit.SECONDS).setDelay(-1L, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testSetDelay_nullTimeUnit() throws Exception {
        new SimpleDelayProvider(0L, TimeUnit.SECONDS).setDelay(1L, null);
    }
}