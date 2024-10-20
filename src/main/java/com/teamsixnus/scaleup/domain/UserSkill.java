package com.teamsixnus.scaleup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserSkill.
 */
@Entity
@Table(name = "user_skill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    private CodeTables skillType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSkill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYearsOfExperience() {
        return this.yearsOfExperience;
    }

    public UserSkill yearsOfExperience(Integer yearsOfExperience) {
        this.setYearsOfExperience(yearsOfExperience);
        return this;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserSkill userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public UserSkill skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public CodeTables getSkillType() {
        return this.skillType;
    }

    public void setSkillType(CodeTables codeTables) {
        this.skillType = codeTables;
    }

    public UserSkill skillType(CodeTables codeTables) {
        this.setSkillType(codeTables);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSkill)) {
            return false;
        }
        return id != null && id.equals(((UserSkill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSkill{" +
            "id=" + getId() +
            ", yearsOfExperience=" + getYearsOfExperience() +
            "}";
    }
}
