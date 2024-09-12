package com.teamsixnus.scaleup.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teamsixnus.scaleup.domain.UserProfile} entity. This class is used
 * in {@link com.teamsixnus.scaleup.web.rest.UserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nickname;

    private StringFilter jobRole;

    private StringFilter aboutMe;

    private StringFilter profilePicture;

    private StringFilter socialLinks;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter userId;

    private Boolean distinct;

    public UserProfileCriteria() {}

    public UserProfileCriteria(UserProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nickname = other.optionalNickname().map(StringFilter::copy).orElse(null);
        this.jobRole = other.optionalJobRole().map(StringFilter::copy).orElse(null);
        this.aboutMe = other.optionalAboutMe().map(StringFilter::copy).orElse(null);
        this.profilePicture = other.optionalProfilePicture().map(StringFilter::copy).orElse(null);
        this.socialLinks = other.optionalSocialLinks().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserProfileCriteria copy() {
        return new UserProfileCriteria(this);
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

    public StringFilter getNickname() {
        return nickname;
    }

    public Optional<StringFilter> optionalNickname() {
        return Optional.ofNullable(nickname);
    }

    public StringFilter nickname() {
        if (nickname == null) {
            setNickname(new StringFilter());
        }
        return nickname;
    }

    public void setNickname(StringFilter nickname) {
        this.nickname = nickname;
    }

    public StringFilter getJobRole() {
        return jobRole;
    }

    public Optional<StringFilter> optionalJobRole() {
        return Optional.ofNullable(jobRole);
    }

    public StringFilter jobRole() {
        if (jobRole == null) {
            setJobRole(new StringFilter());
        }
        return jobRole;
    }

    public void setJobRole(StringFilter jobRole) {
        this.jobRole = jobRole;
    }

    public StringFilter getAboutMe() {
        return aboutMe;
    }

    public Optional<StringFilter> optionalAboutMe() {
        return Optional.ofNullable(aboutMe);
    }

    public StringFilter aboutMe() {
        if (aboutMe == null) {
            setAboutMe(new StringFilter());
        }
        return aboutMe;
    }

    public void setAboutMe(StringFilter aboutMe) {
        this.aboutMe = aboutMe;
    }

    public StringFilter getProfilePicture() {
        return profilePicture;
    }

    public Optional<StringFilter> optionalProfilePicture() {
        return Optional.ofNullable(profilePicture);
    }

    public StringFilter profilePicture() {
        if (profilePicture == null) {
            setProfilePicture(new StringFilter());
        }
        return profilePicture;
    }

    public void setProfilePicture(StringFilter profilePicture) {
        this.profilePicture = profilePicture;
    }

    public StringFilter getSocialLinks() {
        return socialLinks;
    }

    public Optional<StringFilter> optionalSocialLinks() {
        return Optional.ofNullable(socialLinks);
    }

    public StringFilter socialLinks() {
        if (socialLinks == null) {
            setSocialLinks(new StringFilter());
        }
        return socialLinks;
    }

    public void setSocialLinks(StringFilter socialLinks) {
        this.socialLinks = socialLinks;
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

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final UserProfileCriteria that = (UserProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nickname, that.nickname) &&
            Objects.equals(jobRole, that.jobRole) &&
            Objects.equals(aboutMe, that.aboutMe) &&
            Objects.equals(profilePicture, that.profilePicture) &&
            Objects.equals(socialLinks, that.socialLinks) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nickname,
            jobRole,
            aboutMe,
            profilePicture,
            socialLinks,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNickname().map(f -> "nickname=" + f + ", ").orElse("") +
            optionalJobRole().map(f -> "jobRole=" + f + ", ").orElse("") +
            optionalAboutMe().map(f -> "aboutMe=" + f + ", ").orElse("") +
            optionalProfilePicture().map(f -> "profilePicture=" + f + ", ").orElse("") +
            optionalSocialLinks().map(f -> "socialLinks=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
