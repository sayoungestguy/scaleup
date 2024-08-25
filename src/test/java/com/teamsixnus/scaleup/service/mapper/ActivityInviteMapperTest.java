package com.teamsixnus.scaleup.service.mapper;

import static com.teamsixnus.scaleup.domain.ActivityInviteAsserts.*;
import static com.teamsixnus.scaleup.domain.ActivityInviteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivityInviteMapperTest {

    private ActivityInviteMapper activityInviteMapper;

    @BeforeEach
    void setUp() {
        activityInviteMapper = new ActivityInviteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getActivityInviteSample1();
        var actual = activityInviteMapper.toEntity(activityInviteMapper.toDto(expected));
        assertActivityInviteAllPropertiesEquals(expected, actual);
    }
}
