package com.teamsixnus.scaleup.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SkillAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillAllPropertiesEquals(Skill expected, Skill actual) {
        assertSkillAutoGeneratedPropertiesEquals(expected, actual);
        assertSkillAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillAllUpdatablePropertiesEquals(Skill expected, Skill actual) {
        assertSkillUpdatableFieldsEquals(expected, actual);
        assertSkillUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillAutoGeneratedPropertiesEquals(Skill expected, Skill actual) {
        assertThat(expected)
            .as("Verify Skill auto generated properties")
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
    public static void assertSkillUpdatableFieldsEquals(Skill expected, Skill actual) {
        assertThat(expected)
            .as("Verify Skill relevant properties")
            .satisfies(e -> assertThat(e.getSkillName()).as("check skillName").isEqualTo(actual.getSkillName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillUpdatableRelationshipsEquals(Skill expected, Skill actual) {}
}
