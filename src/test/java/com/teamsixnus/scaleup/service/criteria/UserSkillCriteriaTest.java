package com.teamsixnus.scaleup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserSkillCriteriaTest {

    @Test
    void newUserSkillCriteriaHasAllFiltersNullTest() {
        var userSkillCriteria = new UserSkillCriteria();
        assertThat(userSkillCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userSkillCriteriaFluentMethodsCreatesFiltersTest() {
        var userSkillCriteria = new UserSkillCriteria();

        setAllFilters(userSkillCriteria);

        assertThat(userSkillCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userSkillCriteriaCopyCreatesNullFilterTest() {
        var userSkillCriteria = new UserSkillCriteria();
        var copy = userSkillCriteria.copy();

        assertThat(userSkillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userSkillCriteria)
        );
    }

    @Test
    void userSkillCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userSkillCriteria = new UserSkillCriteria();
        setAllFilters(userSkillCriteria);

        var copy = userSkillCriteria.copy();

        assertThat(userSkillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userSkillCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userSkillCriteria = new UserSkillCriteria();

        assertThat(userSkillCriteria).hasToString("UserSkillCriteria{}");
    }

    private static void setAllFilters(UserSkillCriteria userSkillCriteria) {
        userSkillCriteria.id();
        userSkillCriteria.yearsOfExperience();
        userSkillCriteria.userProfileId();
        userSkillCriteria.skillId();
        userSkillCriteria.skillTypeId();
        userSkillCriteria.distinct();
    }

    private static Condition<UserSkillCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getYearsOfExperience()) &&
                condition.apply(criteria.getUserProfileId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getSkillTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserSkillCriteria> copyFiltersAre(UserSkillCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getYearsOfExperience(), copy.getYearsOfExperience()) &&
                condition.apply(criteria.getUserProfileId(), copy.getUserProfileId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getSkillTypeId(), copy.getSkillTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
