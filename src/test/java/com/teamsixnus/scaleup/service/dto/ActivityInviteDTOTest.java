package com.teamsixnus.scaleup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivityInviteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityInviteDTO.class);
        ActivityInviteDTO activityInviteDTO1 = new ActivityInviteDTO();
        activityInviteDTO1.setId(1L);
        ActivityInviteDTO activityInviteDTO2 = new ActivityInviteDTO();
        assertThat(activityInviteDTO1).isNotEqualTo(activityInviteDTO2);
        activityInviteDTO2.setId(activityInviteDTO1.getId());
        assertThat(activityInviteDTO1).isEqualTo(activityInviteDTO2);
        activityInviteDTO2.setId(2L);
        assertThat(activityInviteDTO1).isNotEqualTo(activityInviteDTO2);
        activityInviteDTO1.setId(null);
        assertThat(activityInviteDTO1).isNotEqualTo(activityInviteDTO2);
    }
}
