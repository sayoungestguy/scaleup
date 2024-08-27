package com.teamsixnus.scaleup.domain;

import static com.teamsixnus.scaleup.domain.SkillTestSamples.*;
import static com.teamsixnus.scaleup.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skill.class);
        Skill skill1 = getSkillSample1();
        Skill skill2 = new Skill();
        assertThat(skill1).isNotEqualTo(skill2);

        skill2.setId(skill1.getId());
        assertThat(skill1).isEqualTo(skill2);

        skill2 = getSkillSample2();
        assertThat(skill1).isNotEqualTo(skill2);
    }

    @Test
    void userProfileTest() {
        Skill skill = getSkillRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        skill.setUserProfile(userProfileBack);
        assertThat(skill.getUserProfile()).isEqualTo(userProfileBack);

        skill.userProfile(null);
        assertThat(skill.getUserProfile()).isNull();
    }
}
