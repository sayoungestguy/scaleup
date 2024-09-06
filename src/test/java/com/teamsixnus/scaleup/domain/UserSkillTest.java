package com.teamsixnus.scaleup.domain;

import static com.teamsixnus.scaleup.domain.CodeTablesTestSamples.*;
import static com.teamsixnus.scaleup.domain.SkillTestSamples.*;
import static com.teamsixnus.scaleup.domain.UserProfileTestSamples.*;
import static com.teamsixnus.scaleup.domain.UserSkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSkill.class);
        UserSkill userSkill1 = getUserSkillSample1();
        UserSkill userSkill2 = new UserSkill();
        assertThat(userSkill1).isNotEqualTo(userSkill2);

        userSkill2.setId(userSkill1.getId());
        assertThat(userSkill1).isEqualTo(userSkill2);

        userSkill2 = getUserSkillSample2();
        assertThat(userSkill1).isNotEqualTo(userSkill2);
    }

    @Test
    void userProfileTest() {
        UserSkill userSkill = getUserSkillRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userSkill.setUserProfile(userProfileBack);
        assertThat(userSkill.getUserProfile()).isEqualTo(userProfileBack);

        userSkill.userProfile(null);
        assertThat(userSkill.getUserProfile()).isNull();
    }

    @Test
    void skillTest() {
        UserSkill userSkill = getUserSkillRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        userSkill.setSkill(skillBack);
        assertThat(userSkill.getSkill()).isEqualTo(skillBack);

        userSkill.skill(null);
        assertThat(userSkill.getSkill()).isNull();
    }

    @Test
    void skillTypeTest() {
        UserSkill userSkill = getUserSkillRandomSampleGenerator();
        CodeTables codeTablesBack = getCodeTablesRandomSampleGenerator();

        userSkill.setSkillType(codeTablesBack);
        assertThat(userSkill.getSkillType()).isEqualTo(codeTablesBack);

        userSkill.skillType(null);
        assertThat(userSkill.getSkillType()).isNull();
    }
}
