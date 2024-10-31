package com.teamsixnus.scaleup.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teamsixnus.scaleup.domain.Activity} entity. This class is used
 * in {@link com.teamsixnus.scaleup.web.rest.ActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter activityName;

    private InstantFilter activityTime;

    private IntegerFilter duration;

    private StringFilter venue;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter creatorProfileId;

    private LongFilter skillId;

    private Boolean distinct;

    public ActivityCriteria() {}

    public ActivityCriteria(ActivityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.activityName = other.optionalActivityName().map(StringFilter::copy).orElse(null);
        this.activityTime = other.optionalActivityTime().map(InstantFilter::copy).orElse(null);
        this.duration = other.optionalDuration().map(IntegerFilter::copy).orElse(null);
        this.venue = other.optionalVenue().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.creatorProfileId = other.optionalCreatorProfileId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ActivityCriteria copy() {
        return new ActivityCriteria(this);
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

    public StringFilter getActivityName() {
        return activityName;
    }

    public Optional<StringFilter> optionalActivityName() {
        return Optional.ofNullable(activityName);
    }

    public StringFilter activityName() {
        if (activityName == null) {
            setActivityName(new StringFilter());
        }
        return activityName;
    }

    public void setActivityName(StringFilter activityName) {
        this.activityName = activityName;
    }

    public InstantFilter getActivityTime() {
        return activityTime;
    }

    public Optional<InstantFilter> optionalActivityTime() {
        return Optional.ofNullable(activityTime);
    }

    public InstantFilter activityTime() {
        if (activityTime == null) {
            setActivityTime(new InstantFilter());
        }
        return activityTime;
    }

    public void setActivityTime(InstantFilter activityTime) {
        this.activityTime = activityTime;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public Optional<IntegerFilter> optionalDuration() {
        return Optional.ofNullable(duration);
    }

    public IntegerFilter duration() {
        if (duration == null) {
            setDuration(new IntegerFilter());
        }
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public StringFilter getVenue() {
        return venue;
    }

    public Optional<StringFilter> optionalVenue() {
        return Optional.ofNullable(venue);
    }

    public StringFilter venue() {
        if (venue == null) {
            setVenue(new StringFilter());
        }
        return venue;
    }

    public void setVenue(StringFilter venue) {
        this.venue = venue;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getCreatorProfileId() {
        return creatorProfileId;
    }

    public Optional<LongFilter> optionalCreatorProfileId() {
        return Optional.ofNullable(creatorProfileId);
    }

    public LongFilter creatorProfileId() {
        if (creatorProfileId == null) {
            setCreatorProfileId(new LongFilter());
        }
        return creatorProfileId;
    }

    public void setCreatorProfileId(LongFilter creatorProfileId) {
        this.creatorProfileId = creatorProfileId;
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
        final ActivityCriteria that = (ActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(activityName, that.activityName) &&
            Objects.equals(activityTime, that.activityTime) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(venue, that.venue) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(creatorProfileId, that.creatorProfileId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            activityName,
            activityTime,
            duration,
            venue,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            creatorProfileId,
            skillId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalActivityName().map(f -> "activityName=" + f + ", ").orElse("") +
            optionalActivityTime().map(f -> "activityTime=" + f + ", ").orElse("") +
            optionalDuration().map(f -> "duration=" + f + ", ").orElse("") +
            optionalVenue().map(f -> "venue=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalCreatorProfileId().map(f -> "creatorProfileId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
