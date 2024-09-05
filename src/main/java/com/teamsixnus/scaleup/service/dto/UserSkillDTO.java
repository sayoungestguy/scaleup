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
    @Min(value = 0)
    @Max(value = 100)
    private Integer experience;

    private UserProfileDTO userProfile;

    private SkillDTO skill;

    private CodeTablesDTO codeTables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
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

    public CodeTablesDTO getCodeTables() {
        return codeTables;
    }

    public void setCodeTables(CodeTablesDTO codeTables) {
        this.codeTables = codeTables;
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
            ", experience=" + getExperience() +
            ", userProfile=" + getUserProfile() +
            ", skill=" + getSkill() +
            ", codeTables=" + getCodeTables() +
            "}";
    }
}
