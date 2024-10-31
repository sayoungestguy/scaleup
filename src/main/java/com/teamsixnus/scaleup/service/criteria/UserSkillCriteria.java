package com.teamsixnus.scaleup.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teamsixnus.scaleup.domain.UserSkill} entity. This class is used
 * in {@link com.teamsixnus.scaleup.web.rest.UserSkillResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-skills?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSkillCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter yearsOfExperience;

    private LongFilter userProfileId;

    private LongFilter skillId;

    private LongFilter skillTypeId;

    private Boolean distinct;

    public UserSkillCriteria() {}

    public UserSkillCriteria(UserSkillCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.yearsOfExperience = other.optionalYearsOfExperience().map(IntegerFilter::copy).orElse(null);
        this.userProfileId = other.optionalUserProfileId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.skillTypeId = other.optionalSkillTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserSkillCriteria copy() {
        return new UserSkillCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getYearsOfExperience() {
        return yearsOfExperience;
    }

    public Optional<IntegerFilter> optionalYearsOfExperience() {
        return Optional.ofNullable(yearsOfExperience);
    }

    public IntegerFilter yearsOfExperience() {
        if (yearsOfExperience == null) {
            setYearsOfExperience(new IntegerFilter());
        }
        return yearsOfExperience;
    }

    public void setYearsOfExperience(IntegerFilter yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public Optional<LongFilter> optionalUserProfileId() {
        return Optional.ofNullable(userProfileId);
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            setUserProfileId(new LongFilter());
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getSkillId() {
        return skillId;
    }

    public Optional<LongFilter> optionalSkillId() {
        return Optional.ofNullable(skillId);
    }

    public LongFilter skillId() {
        if (skillId == null) {
            setSkillId(new LongFilter());
        }
        return skillId;
    }

    public void setSkillId(LongFilter skillId) {
        this.skillId = skillId;
    }

    public LongFilter getSkillTypeId() {
        return skillTypeId;
    }

    public Optional<LongFilter> optionalSkillTypeId() {
        return Optional.ofNullable(skillTypeId);
    }

    public LongFilter skillTypeId() {
        if (skillTypeId == null) {
            setSkillTypeId(new LongFilter());
        }
        return skillTypeId;
    }

    public void setSkillTypeId(LongFilter skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserSkillCriteria that = (UserSkillCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(yearsOfExperience, that.yearsOfExperience) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(skillTypeId, that.skillTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, yearsOfExperience, userProfileId, skillId, skillTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSkillCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalYearsOfExperience().map(f -> "yearsOfExperience=" + f + ", ").orElse("") +
            optionalUserProfileId().map(f -> "userProfileId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalSkillTypeId().map(f -> "skillTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
