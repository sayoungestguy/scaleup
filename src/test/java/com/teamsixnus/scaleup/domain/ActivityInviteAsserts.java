package com.teamsixnus.scaleup.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityInviteAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityInviteAllPropertiesEquals(ActivityInvite expected, ActivityInvite actual) {
        assertActivityInviteAutoGeneratedPropertiesEquals(expected, actual);
        assertActivityInviteAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityInviteAllUpdatablePropertiesEquals(ActivityInvite expected, ActivityInvite actual) {
        assertActivityInviteUpdatableFieldsEquals(expected, actual);
        assertActivityInviteUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityInviteAutoGeneratedPropertiesEquals(ActivityInvite expected, ActivityInvite actual) {
        assertThat(expected)
            .as("Verify ActivityInvite auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityInviteUpdatableFieldsEquals(ActivityInvite expected, ActivityInvite actual) {
        assertThat(expected)
            .as("Verify ActivityInvite relevant properties")
            .satisfies(e -> assertThat(e.getWillParticipate()).as("check willParticipate").isEqualTo(actual.getWillParticipate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityInviteUpdatableRelationshipsEquals(ActivityInvite expected, ActivityInvite actual) {
        assertThat(expected)
            .as("Verify ActivityInvite relationships")
            .satisfies(e -> assertThat(e.getActivity()).as("check activity").isEqualTo(actual.getActivity()))
            .satisfies(e -> assertThat(e.getInviteeProfile()).as("check inviteeProfile").isEqualTo(actual.getInviteeProfile()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }
}