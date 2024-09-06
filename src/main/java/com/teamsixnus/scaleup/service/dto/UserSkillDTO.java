package com.teamsixnus.scaleup.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.teamsixnus.scaleup.domain.UserSkill} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSkillDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer yearsOfExperience;

    private UserProfileDTO userProfile;

    private SkillDTO skill;

    private CodeTablesDTO skillType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    public SkillDTO getSkill() {
        return skill;
    }

    public void setSkill(SkillDTO skill) {
        this.skill = skill;
    }

    public CodeTablesDTO getSkillType() {
        return skillType;
    }

    public void setSkillType(CodeTablesDTO skillType) {
        this.skillType = skillType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSkillDTO)) {
            return false;
        }

        UserSkillDTO userSkillDTO = (UserSkillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSkillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSkillDTO{" +
            "id=" + getId() +
            ", yearsOfExperience=" + getYearsOfExperience() +
            ", userProfile=" + getUserProfile() +
            ", skill=" + getSkill() +
            ", skillType=" + getSkillType() +
            "}";
    }
}
