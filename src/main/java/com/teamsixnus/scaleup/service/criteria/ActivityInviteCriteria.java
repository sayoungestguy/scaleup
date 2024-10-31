package com.teamsixnus.scaleup.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teamsixnus.scaleup.domain.ActivityInvite} entity. This class is used
 * in {@link com.teamsixnus.scaleup.web.rest.ActivityInviteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /activity-invites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityInviteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter willParticipate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter activityId;

    private LongFilter inviteeProfileId;

    private LongFilter statusId;

    private Boolean distinct;

    public ActivityInviteCriteria() {}

    public ActivityInviteCriteria(ActivityInviteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.willParticipate = other.optionalWillParticipate().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.activityId = other.optionalActivityId().map(LongFilter::copy).orElse(null);
        this.inviteeProfileId = other.optionalInviteeProfileId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ActivityInviteCriteria copy() {
        return new ActivityInviteCriteria(this);
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

    public BooleanFilter getWillParticipate() {
        return willParticipate;
    }

    public Optional<BooleanFilter> optionalWillParticipate() {
        return Optional.ofNullable(willParticipate);
    }

    public BooleanFilter willParticipate() {
        if (willParticipate == null) {
            setWillParticipate(new BooleanFilter());
        }
        return willParticipate;
    }

    public void setWillParticipate(BooleanFilter willParticipate) {
        this.willParticipate = willParticipate;
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

    public LongFilter getActivityId() {
        return activityId;
    }

    public Optional<LongFilter> optionalActivityId() {
        return Optional.ofNullable(activityId);
    }

    public LongFilter activityId() {
        if (activityId == null) {
            setActivityId(new LongFilter());
        }
        return activityId;
    }

    public void setActivityId(LongFilter activityId) {
        this.activityId = activityId;
    }

    public LongFilter getInviteeProfileId() {
        return inviteeProfileId;
    }

    public Optional<LongFilter> optionalInviteeProfileId() {
        return Optional.ofNullable(inviteeProfileId);
    }

    public LongFilter inviteeProfileId() {
        if (inviteeProfileId == null) {
            setInviteeProfileId(new LongFilter());
        }
        return inviteeProfileId;
    }

    public void setInviteeProfileId(LongFilter inviteeProfileId) {
        this.inviteeProfileId = inviteeProfileId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public Optional<LongFilter> optionalStatusId() {
        return Optional.ofNullable(statusId);
    }

    public LongFilter statusId() {
        if (statusId == null) {
            setStatusId(new LongFilter());
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
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
        final ActivityInviteCriteria that = (ActivityInviteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(willParticipate, that.willParticipate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(activityId, that.activityId) &&
            Objects.equals(inviteeProfileId, that.inviteeProfileId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            willParticipate,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            activityId,
            inviteeProfileId,
            statusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityInviteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalWillParticipate().map(f -> "willParticipate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalActivityId().map(f -> "activityId=" + f + ", ").orElse("") +
            optionalInviteeProfileId().map(f -> "inviteeProfileId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
