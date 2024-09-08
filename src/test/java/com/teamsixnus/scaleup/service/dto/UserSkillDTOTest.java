package com.teamsixnus.scaleup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSkillDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSkillDTO.class);
        UserSkillDTO userSkillDTO1 = new UserSkillDTO();
        userSkillDTO1.setId(1L);
        UserSkillDTO userSkillDTO2 = new UserSkillDTO();
        assertThat(userSkillDTO1).isNotEqualTo(userSkillDTO2);
        userSkillDTO2.setId(userSkillDTO1.getId());
        assertThat(userSkillDTO1).isEqualTo(userSkillDTO2);
        userSkillDTO2.setId(2L);
        assertThat(userSkillDTO1).isNotEqualTo(userSkillDTO2);
        userSkillDTO1.setId(null);
        assertThat(userSkillDTO1).isNotEqualTo(userSkillDTO2);
    }
}
