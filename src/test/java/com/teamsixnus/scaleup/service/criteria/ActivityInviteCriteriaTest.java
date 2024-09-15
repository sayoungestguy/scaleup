package com.teamsixnus.scaleup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ActivityInviteCriteriaTest {

    @Test
    void newActivityInviteCriteriaHasAllFiltersNullTest() {
        var activityInviteCriteria = new ActivityInviteCriteria();
        assertThat(activityInviteCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void activityInviteCriteriaFluentMethodsCreatesFiltersTest() {
        var activityInviteCriteria = new ActivityInviteCriteria();

        setAllFilters(activityInviteCriteria);

        assertThat(activityInviteCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void activityInviteCriteriaCopyCreatesNullFilterTest() {
        var activityInviteCriteria = new ActivityInviteCriteria();
        var copy = activityInviteCriteria.copy();

        assertThat(activityInviteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(activityInviteCriteria)
        );
    }

    @Test
    void activityInviteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var activityInviteCriteria = new ActivityInviteCriteria();
        setAllFilters(activityInviteCriteria);

        var copy = activityInviteCriteria.copy();

        assertThat(activityInviteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(activityInviteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var activityInviteCriteria = new ActivityInviteCriteria();

        assertThat(activityInviteCriteria).hasToString("ActivityInviteCriteria{}");
    }

    private static void setAllFilters(ActivityInviteCriteria activityInviteCriteria) {
        activityInviteCriteria.id();
        activityInviteCriteria.willParticipate();
        activityInviteCriteria.createdBy();
        activityInviteCriteria.createdDate();
        activityInviteCriteria.lastModifiedBy();
        activityInviteCriteria.lastModifiedDate();
        activityInviteCriteria.activityId();
        activityInviteCriteria.inviteeProfileId();
        activityInviteCriteria.statusId();
        activityInviteCriteria.distinct();
    }

    private static Condition<ActivityInviteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getWillParticipate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getActivityId()) &&
                condition.apply(criteria.getInviteeProfileId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ActivityInviteCriteria> copyFiltersAre(
        ActivityInviteCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getWillParticipate(), copy.getWillParticipate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getActivityId(), copy.getActivityId()) &&
                condition.apply(criteria.getInviteeProfileId(), copy.getInviteeProfileId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
