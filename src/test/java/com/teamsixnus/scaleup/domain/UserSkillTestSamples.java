package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserSkillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserSkill getUserSkillSample1() {
        return new UserSkill().id(1L).yearsOfExperience(1);
    }

    public static UserSkill getUserSkillSample2() {
        return new UserSkill().id(2L).yearsOfExperience(2);
    }

    public static UserSkill getUserSkillRandomSampleGenerator() {
        return new UserSkill().id(longCount.incrementAndGet()).yearsOfExperience(intCount.incrementAndGet());
    }
}
