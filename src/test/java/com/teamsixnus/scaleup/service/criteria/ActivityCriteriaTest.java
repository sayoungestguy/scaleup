package com.teamsixnus.scaleup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ActivityCriteriaTest {

    @Test
    void newActivityCriteriaHasAllFiltersNullTest() {
        var activityCriteria = new ActivityCriteria();
        assertThat(activityCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void activityCriteriaFluentMethodsCreatesFiltersTest() {
        var activityCriteria = new ActivityCriteria();

        setAllFilters(activityCriteria);

        assertThat(activityCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void activityCriteriaCopyCreatesNullFilterTest() {
        var activityCriteria = new ActivityCriteria();
        var copy = activityCriteria.copy();

        assertThat(activityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(activityCriteria)
        );
    }

    @Test
    void activityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var activityCriteria = new ActivityCriteria();
        setAllFilters(activityCriteria);

        var copy = activityCriteria.copy();

        assertThat(activityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(activityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var activityCriteria = new ActivityCriteria();

        assertThat(activityCriteria).hasToString("ActivityCriteria{}");
    }

    private static void setAllFilters(ActivityCriteria activityCriteria) {
        activityCriteria.id();
        activityCriteria.activityName();
        activityCriteria.activityTime();
        activityCriteria.duration();
        activityCriteria.venue();
        activityCriteria.createdBy();
        activityCriteria.createdDate();
        activityCriteria.lastModifiedBy();
        activityCriteria.lastModifiedDate();
        activityCriteria.creatorProfileId();
        activityCriteria.skillId();
        activityCriteria.distinct();
    }

    private static Condition<ActivityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getActivityName()) &&
                condition.apply(criteria.getActivityTime()) &&
                condition.apply(criteria.getDuration()) &&
                condition.apply(criteria.getVenue()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getCreatorProfileId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ActivityCriteria> copyFiltersAre(ActivityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getActivityName(), copy.getActivityName()) &&
                condition.apply(criteria.getActivityTime(), copy.getActivityTime()) &&
                condition.apply(criteria.getDuration(), copy.getDuration()) &&
                condition.apply(criteria.getVenue(), copy.getVenue()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getCreatorProfileId(), copy.getCreatorProfileId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
