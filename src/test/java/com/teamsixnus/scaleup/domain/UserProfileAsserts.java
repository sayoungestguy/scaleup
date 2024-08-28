package com.teamsixnus.scaleup.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserProfileAllPropertiesEquals(UserProfile expected, UserProfile actual) {
        assertUserProfileAutoGeneratedPropertiesEquals(expected, actual);
        assertUserProfileAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserProfileAllUpdatablePropertiesEquals(UserProfile expected, UserProfile actual) {
        assertUserProfileUpdatableFieldsEquals(expected, actual);
        assertUserProfileUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserProfileAutoGeneratedPropertiesEquals(UserProfile expected, UserProfile actual) {
        assertThat(expected)
            .as("Verify UserProfile auto generated properties")
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
    public static void assertUserProfileUpdatableFieldsEquals(UserProfile expected, UserProfile actual) {
        assertThat(expected)
            .as("Verify UserProfile relevant properties")
            .satisfies(e -> assertThat(e.getNickname()).as("check nickname").isEqualTo(actual.getNickname()))
            .satisfies(e -> assertThat(e.getJobRole()).as("check jobRole").isEqualTo(actual.getJobRole()))
            .satisfies(e -> assertThat(e.getAboutMe()).as("check aboutMe").isEqualTo(actual.getAboutMe()))
            .satisfies(e -> assertThat(e.getProfilePicture()).as("check profilePicture").isEqualTo(actual.getProfilePicture()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserProfileUpdatableRelationshipsEquals(UserProfile expected, UserProfile actual) {}
}