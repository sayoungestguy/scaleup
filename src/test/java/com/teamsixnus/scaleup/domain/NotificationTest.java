package com.teamsixnus.scaleup.domain;

import static com.teamsixnus.scaleup.domain.CodeTablesTestSamples.*;
import static com.teamsixnus.scaleup.domain.NotificationTestSamples.*;
import static com.teamsixnus.scaleup.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void userProfileTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        notification.setUserProfile(userProfileBack);
        assertThat(notification.getUserProfile()).isEqualTo(userProfileBack);

        notification.userProfile(null);
        assertThat(notification.getUserProfile()).isNull();
    }

    @Test
    void typeTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        CodeTables codeTablesBack = getCodeTablesRandomSampleGenerator();

        notification.setType(codeTablesBack);
        assertThat(notification.getType()).isEqualTo(codeTablesBack);

        notification.type(null);
        assertThat(notification.getType()).isNull();
    }
}
