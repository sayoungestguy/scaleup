package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SkillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Skill getSkillSample1() {
        return new Skill().id(1L).skillName("skillName1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Skill getSkillSample2() {
        return new Skill().id(2L).skillName("skillName2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Skill getSkillRandomSampleGenerator() {
        return new Skill()
            .id(longCount.incrementAndGet())
            .skillName(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
