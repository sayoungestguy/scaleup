package com.teamsixnus.scaleup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SkillCriteriaTest {

    @Test
    void newSkillCriteriaHasAllFiltersNullTest() {
        var skillCriteria = new SkillCriteria();
        assertThat(skillCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void skillCriteriaFluentMethodsCreatesFiltersTest() {
        var skillCriteria = new SkillCriteria();

        setAllFilters(skillCriteria);

        assertThat(skillCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void skillCriteriaCopyCreatesNullFilterTest() {
        var skillCriteria = new SkillCriteria();
        var copy = skillCriteria.copy();

        assertThat(skillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(skillCriteria)
        );
    }

    @Test
    void skillCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var skillCriteria = new SkillCriteria();
        setAllFilters(skillCriteria);

        var copy = skillCriteria.copy();

        assertThat(skillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(skillCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var skillCriteria = new SkillCriteria();

        assertThat(skillCriteria).hasToString("SkillCriteria{}");
    }

    private static void setAllFilters(SkillCriteria skillCriteria) {
        skillCriteria.id();
        skillCriteria.skillName();
        skillCriteria.createdBy();
        skillCriteria.createdDate();
        skillCriteria.lastModifiedBy();
        skillCriteria.lastModifiedDate();
        skillCriteria.distinct();
    }

    private static Condition<SkillCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSkillName()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SkillCriteria> copyFiltersAre(SkillCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSkillName(), copy.getSkillName()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
