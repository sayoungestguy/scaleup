package com.teamsixnus.scaleup.domain;

import static com.teamsixnus.scaleup.domain.ActivityTestSamples.*;
import static com.teamsixnus.scaleup.domain.SkillTestSamples.*;
import static com.teamsixnus.scaleup.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
        Activity activity1 = getActivitySample1();
        Activity activity2 = new Activity();
        assertThat(activity1).isNotEqualTo(activity2);

        activity2.setId(activity1.getId());
        assertThat(activity1).isEqualTo(activity2);

        activity2 = getActivitySample2();
        assertThat(activity1).isNotEqualTo(activity2);
    }

    @Test
    void creatorProfileTest() {
        Activity activity = getActivityRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        activity.setCreatorProfile(userProfileBack);
        assertThat(activity.getCreatorProfile()).isEqualTo(userProfileBack);

        activity.creatorProfile(null);
        assertThat(activity.getCreatorProfile()).isNull();
    }

    @Test
    void skillTest() {
        Activity activity = getActivityRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        activity.setSkill(skillBack);
        assertThat(activity.getSkill()).isEqualTo(skillBack);

        activity.skill(null);
        assertThat(activity.getSkill()).isNull();
    }
}
