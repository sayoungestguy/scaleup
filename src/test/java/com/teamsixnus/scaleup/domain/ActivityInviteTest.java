package com.teamsixnus.scaleup.domain;

import static com.teamsixnus.scaleup.domain.ActivityInviteTestSamples.*;
import static com.teamsixnus.scaleup.domain.ActivityTestSamples.*;
import static com.teamsixnus.scaleup.domain.CodeTablesTestSamples.*;
import static com.teamsixnus.scaleup.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivityInviteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityInvite.class);
        ActivityInvite activityInvite1 = getActivityInviteSample1();
        ActivityInvite activityInvite2 = new ActivityInvite();
        assertThat(activityInvite1).isNotEqualTo(activityInvite2);

        activityInvite2.setId(activityInvite1.getId());
        assertThat(activityInvite1).isEqualTo(activityInvite2);

        activityInvite2 = getActivityInviteSample2();
        assertThat(activityInvite1).isNotEqualTo(activityInvite2);
    }

    @Test
    void activityTest() {
        ActivityInvite activityInvite = getActivityInviteRandomSampleGenerator();
        Activity activityBack = getActivityRandomSampleGenerator();

        activityInvite.setActivity(activityBack);
        assertThat(activityInvite.getActivity()).isEqualTo(activityBack);

        activityInvite.activity(null);
        assertThat(activityInvite.getActivity()).isNull();
    }

    @Test
    void inviteeProfileTest() {
        ActivityInvite activityInvite = getActivityInviteRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        activityInvite.setInviteeProfile(userProfileBack);
        assertThat(activityInvite.getInviteeProfile()).isEqualTo(userProfileBack);

        activityInvite.inviteeProfile(null);
        assertThat(activityInvite.getInviteeProfile()).isNull();
    }

    @Test
    void statusTest() {
        ActivityInvite activityInvite = getActivityInviteRandomSampleGenerator();
        CodeTables codeTablesBack = getCodeTablesRandomSampleGenerator();

        activityInvite.setStatus(codeTablesBack);
        assertThat(activityInvite.getStatus()).isEqualTo(codeTablesBack);

        activityInvite.status(null);
        assertThat(activityInvite.getStatus()).isNull();
    }
}
