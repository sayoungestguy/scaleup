package com.teamsixnus.scaleup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeTablesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeTablesDTO.class);
        CodeTablesDTO codeTablesDTO1 = new CodeTablesDTO();
        codeTablesDTO1.setId(1L);
        CodeTablesDTO codeTablesDTO2 = new CodeTablesDTO();
        assertThat(codeTablesDTO1).isNotEqualTo(codeTablesDTO2);
        codeTablesDTO2.setId(codeTablesDTO1.getId());
        assertThat(codeTablesDTO1).isEqualTo(codeTablesDTO2);
        codeTablesDTO2.setId(2L);
        assertThat(codeTablesDTO1).isNotEqualTo(codeTablesDTO2);
        codeTablesDTO1.setId(null);
        assertThat(codeTablesDTO1).isNotEqualTo(codeTablesDTO2);
    }
}
