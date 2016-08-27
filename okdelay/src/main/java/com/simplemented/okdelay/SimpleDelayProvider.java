package com.simplemented.okdelay;

import java.util.concurrent.TimeUnit;

public class SimpleDelayProvider implements DelayInterceptor.DelayProvider {

    private long duration;
    private TimeUnit timeUnit;

    private static long checkDuration(final long duration) {
        if (duration < 0) throw new IllegalArgumentException("duration cannot be less than zero.");
        return duration;
    }

    private static TimeUnit checkTimeUnit(final TimeUnit timeUnit) {
        if (timeUnit == null) throw new NullPointerException("timeUnit cannot be null.");
        return timeUnit;
    }

    public SimpleDelayProvider(final long duration, final TimeUnit timeUnit) {
        this.duration = checkDuration(duration);
        this.timeUnit = checkTimeUnit(timeUnit);
    }

    public void setDelay(final long duration, final TimeUnit timeUnit) {
        this.duration = checkDuration(duration);
        this.timeUnit = checkTimeUnit(timeUnit);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(final long duration) {
        this.duration = checkDuration(duration);
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(final TimeUnit timeUnit) {
        this.timeUnit = checkTimeUnit(timeUnit);
    }

    @Override
    public long getDelay() {
        return timeUnit.toMillis(duration);
    }
}
