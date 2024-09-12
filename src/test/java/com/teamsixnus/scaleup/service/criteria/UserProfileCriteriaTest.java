package com.teamsixnus.scaleup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserProfileCriteriaTest {

    @Test
    void newUserProfileCriteriaHasAllFiltersNullTest() {
        var userProfileCriteria = new UserProfileCriteria();
        assertThat(userProfileCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var userProfileCriteria = new UserProfileCriteria();

        setAllFilters(userProfileCriteria);

        assertThat(userProfileCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userProfileCriteriaCopyCreatesNullFilterTest() {
        var userProfileCriteria = new UserProfileCriteria();
        var copy = userProfileCriteria.copy();

        assertThat(userProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userProfileCriteria)
        );
    }

    @Test
    void userProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userProfileCriteria = new UserProfileCriteria();
        setAllFilters(userProfileCriteria);

        var copy = userProfileCriteria.copy();

        assertThat(userProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userProfileCriteria = new UserProfileCriteria();

        assertThat(userProfileCriteria).hasToString("UserProfileCriteria{}");
    }

    private static void setAllFilters(UserProfileCriteria userProfileCriteria) {
        userProfileCriteria.id();
        userProfileCriteria.nickname();
        userProfileCriteria.jobRole();
        userProfileCriteria.aboutMe();
        userProfileCriteria.profilePicture();
        userProfileCriteria.socialLinks();
        userProfileCriteria.createdBy();
        userProfileCriteria.createdDate();
        userProfileCriteria.lastModifiedBy();
        userProfileCriteria.lastModifiedDate();
        userProfileCriteria.userId();
        userProfileCriteria.distinct();
    }

    private static Condition<UserProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNickname()) &&
                condition.apply(criteria.getJobRole()) &&
                condition.apply(criteria.getAboutMe()) &&
                condition.apply(criteria.getProfilePicture()) &&
                condition.apply(criteria.getSocialLinks()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserProfileCriteria> copyFiltersAre(UserProfileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNickname(), copy.getNickname()) &&
                condition.apply(criteria.getJobRole(), copy.getJobRole()) &&
                condition.apply(criteria.getAboutMe(), copy.getAboutMe()) &&
                condition.apply(criteria.getProfilePicture(), copy.getProfilePicture()) &&
                condition.apply(criteria.getSocialLinks(), copy.getSocialLinks()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
