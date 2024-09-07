package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SkillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Skill getSkillSample1() {
        return new Skill()
            .id(1L)
            .skillName("skillName1")
            .individualSkillDesc("individualSkillDesc1")
            .yearsOfExp(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Skill getSkillSample2() {
        return new Skill()
            .id(2L)
            .skillName("skillName2")
            .individualSkillDesc("individualSkillDesc2")
            .yearsOfExp(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Skill getSkillRandomSampleGenerator() {
        return new Skill()
            .id(longCount.incrementAndGet())
            .skillName(UUID.randomUUID().toString())
            .individualSkillDesc(UUID.randomUUID().toString())
            .yearsOfExp(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
