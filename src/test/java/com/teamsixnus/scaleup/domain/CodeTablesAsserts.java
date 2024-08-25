package com.teamsixnus.scaleup.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeTablesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCodeTablesAllPropertiesEquals(CodeTables expected, CodeTables actual) {
        assertCodeTablesAutoGeneratedPropertiesEquals(expected, actual);
        assertCodeTablesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCodeTablesAllUpdatablePropertiesEquals(CodeTables expected, CodeTables actual) {
        assertCodeTablesUpdatableFieldsEquals(expected, actual);
        assertCodeTablesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCodeTablesAutoGeneratedPropertiesEquals(CodeTables expected, CodeTables actual) {
        assertThat(expected)
            .as("Verify CodeTables auto generated properties")
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
    public static void assertCodeTablesUpdatableFieldsEquals(CodeTables expected, CodeTables actual) {
        assertThat(expected)
            .as("Verify CodeTables relevant properties")
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()))
            .satisfies(e -> assertThat(e.getCodeKey()).as("check codeKey").isEqualTo(actual.getCodeKey()))
            .satisfies(e -> assertThat(e.getCodeValue()).as("check codeValue").isEqualTo(actual.getCodeValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCodeTablesUpdatableRelationshipsEquals(CodeTables expected, CodeTables actual) {}
}
