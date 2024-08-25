package com.teamsixnus.scaleup.domain;

import static com.teamsixnus.scaleup.domain.CodeTablesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teamsixnus.scaleup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeTablesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeTables.class);
        CodeTables codeTables1 = getCodeTablesSample1();
        CodeTables codeTables2 = new CodeTables();
        assertThat(codeTables1).isNotEqualTo(codeTables2);

        codeTables2.setId(codeTables1.getId());
        assertThat(codeTables1).isEqualTo(codeTables2);

        codeTables2 = getCodeTablesSample2();
        assertThat(codeTables1).isNotEqualTo(codeTables2);
    }
}
