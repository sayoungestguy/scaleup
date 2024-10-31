package com.teamsixnus.scaleup.domain;

import static com.teamsixnus.scaleup.domain.MessageTestSamples.*;
import static com.teamsixnus.scaleup.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = getMessageSample1();
        Message message2 = new Message();
        assertThat(message1).isNotEqualTo(message2);

        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);

        message2 = getMessageSample2();
        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    void senderProfileTest() {
        Message message = getMessageRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        message.setSenderProfile(userProfileBack);
        assertThat(message.getSenderProfile()).isEqualTo(userProfileBack);

        message.senderProfile(null);
        assertThat(message.getSenderProfile()).isNull();
    }

    @Test
    void receiverProfileTest() {
        Message message = getMessageRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        message.setReceiverProfile(userProfileBack);
        assertThat(message.getReceiverProfile()).isEqualTo(userProfileBack);

        message.receiverProfile(null);
        assertThat(message.getReceiverProfile()).isNull();
    }
}
