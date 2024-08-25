package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Activity getActivitySample1() {
        return new Activity().id(1L).duration(1).venue("venue1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Activity getActivitySample2() {
        return new Activity().id(2L).duration(2).venue("venue2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Activity getActivityRandomSampleGenerator() {
        return new Activity()
            .id(longCount.incrementAndGet())
            .duration(intCount.incrementAndGet())
            .venue(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
