package com.teamsixnus.scaleup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CodeTablesCriteriaTest {

    @Test
    void newCodeTablesCriteriaHasAllFiltersNullTest() {
        var codeTablesCriteria = new CodeTablesCriteria();
        assertThat(codeTablesCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void codeTablesCriteriaFluentMethodsCreatesFiltersTest() {
        var codeTablesCriteria = new CodeTablesCriteria();

        setAllFilters(codeTablesCriteria);

        assertThat(codeTablesCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void codeTablesCriteriaCopyCreatesNullFilterTest() {
        var codeTablesCriteria = new CodeTablesCriteria();
        var copy = codeTablesCriteria.copy();

        assertThat(codeTablesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(codeTablesCriteria)
        );
    }

    @Test
    void codeTablesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var codeTablesCriteria = new CodeTablesCriteria();
        setAllFilters(codeTablesCriteria);

        var copy = codeTablesCriteria.copy();

        assertThat(codeTablesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(codeTablesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var codeTablesCriteria = new CodeTablesCriteria();

        assertThat(codeTablesCriteria).hasToString("CodeTablesCriteria{}");
    }

    private static void setAllFilters(CodeTablesCriteria codeTablesCriteria) {
        codeTablesCriteria.id();
        codeTablesCriteria.category();
        codeTablesCriteria.codeKey();
        codeTablesCriteria.codeValue();
        codeTablesCriteria.createdBy();
        codeTablesCriteria.createdDate();
        codeTablesCriteria.lastModifiedBy();
        codeTablesCriteria.lastModifiedDate();
        codeTablesCriteria.distinct();
    }

    private static Condition<CodeTablesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getCodeKey()) &&
                condition.apply(criteria.getCodeValue()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CodeTablesCriteria> copyFiltersAre(CodeTablesCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getCodeKey(), copy.getCodeKey()) &&
                condition.apply(criteria.getCodeValue(), copy.getCodeValue()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
