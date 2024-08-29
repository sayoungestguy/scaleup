package com.teamsixnus.scaleup.service.mapper;

import static com.teamsixnus.scaleup.domain.CodeTablesAsserts.*;
import static com.teamsixnus.scaleup.domain.CodeTablesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeTablesMapperTest {

    private CodeTablesMapper codeTablesMapper;

    @BeforeEach
    void setUp() {
        codeTablesMapper = new CodeTablesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCodeTablesSample1();
        var actual = codeTablesMapper.toEntity(codeTablesMapper.toDto(expected));
        assertCodeTablesAllPropertiesEquals(expected, actual);
    }
}
