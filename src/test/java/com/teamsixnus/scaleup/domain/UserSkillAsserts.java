package com.teamsixnus.scaleup.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSkillAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserSkillAllPropertiesEquals(UserSkill expected, UserSkill actual) {
        assertUserSkillAutoGeneratedPropertiesEquals(expected, actual);
        assertUserSkillAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserSkillAllUpdatablePropertiesEquals(UserSkill expected, UserSkill actual) {
        assertUserSkillUpdatableFieldsEquals(expected, actual);
        assertUserSkillUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserSkillAutoGeneratedPropertiesEquals(UserSkill expected, UserSkill actual) {
        assertThat(expected)
            .as("Verify UserSkill auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserSkillUpdatableFieldsEquals(UserSkill expected, UserSkill actual) {
        assertThat(expected)
            .as("Verify UserSkill relevant properties")
            .satisfies(e -> assertThat(e.getYearsOfExperience()).as("check yearsOfExperience").isEqualTo(actual.getYearsOfExperience()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserSkillUpdatableRelationshipsEquals(UserSkill expected, UserSkill actual) {
        assertThat(expected)
            .as("Verify UserSkill relationships")
            .satisfies(e -> assertThat(e.getUserProfile()).as("check userProfile").isEqualTo(actual.getUserProfile()))
            .satisfies(e -> assertThat(e.getSkill()).as("check skill").isEqualTo(actual.getSkill()))
            .satisfies(e -> assertThat(e.getSkillType()).as("check skillType").isEqualTo(actual.getSkillType()));
    }
}