package com.teamsixnus.scaleup.service.mapper;

import static com.teamsixnus.scaleup.domain.SkillAsserts.*;
import static com.teamsixnus.scaleup.domain.SkillTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillMapperTest {

    private SkillMapper skillMapper;

    @BeforeEach
    void setUp() {
        skillMapper = new SkillMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSkillSample1();
        var actual = skillMapper.toEntity(skillMapper.toDto(expected));
        assertSkillAllPropertiesEquals(expected, actual);
    }
}
