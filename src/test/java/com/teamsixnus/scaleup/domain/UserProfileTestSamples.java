package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile()
            .id(1L)
            .nickname("nickname1")
            .jobRole("jobRole1")
            .aboutMe("aboutMe1")
            .profilePicture("profilePicture1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile()
            .id(2L)
            .nickname("nickname2")
            .jobRole("jobRole2")
            .aboutMe("aboutMe2")
            .profilePicture("profilePicture2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .nickname(UUID.randomUUID().toString())
            .jobRole(UUID.randomUUID().toString())
            .aboutMe(UUID.randomUUID().toString())
            .profilePicture(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
