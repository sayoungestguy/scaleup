package com.teamsixnus.scaleup.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teamsixnus.scaleup.domain.ActivityInvite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityInviteDTO implements Serializable {

    private Long id;

    private Boolean willParticipate;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private ActivityDTO activity;

    private UserProfileDTO inviteeProfile;

    private CodeTablesDTO status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getWillParticipate() {
        return willParticipate;
    }

    public void setWillParticipate(Boolean willParticipate) {
        this.willParticipate = willParticipate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    public UserProfileDTO getInviteeProfile() {
        return inviteeProfile;
    }

    public void setInviteeProfile(UserProfileDTO inviteeProfile) {
        this.inviteeProfile = inviteeProfile;
    }

    public CodeTablesDTO getStatus() {
        return status;
    }

    public void setStatus(CodeTablesDTO status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityInviteDTO)) {
            return false;
        }

        ActivityInviteDTO activityInviteDTO = (ActivityInviteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, activityInviteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityInviteDTO{" +
            "id=" + getId() +
            ", willParticipate='" + getWillParticipate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", activity=" + getActivity() +
            ", inviteeProfile=" + getInviteeProfile() +
            ", status=" + getStatus() +
            "}";
    }
}
