package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActivityInviteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ActivityInvite getActivityInviteSample1() {
        return new ActivityInvite().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static ActivityInvite getActivityInviteSample2() {
        return new ActivityInvite().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static ActivityInvite getActivityInviteRandomSampleGenerator() {
        return new ActivityInvite()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
