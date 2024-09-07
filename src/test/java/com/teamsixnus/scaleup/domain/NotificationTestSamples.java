package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification()
            .id(1L)
            .notificationRefId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Notification getNotificationSample2() {
        return new Notification()
            .id(2L)
            .notificationRefId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .notificationRefId(UUID.randomUUID())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
