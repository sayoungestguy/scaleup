package com.teamsixnus.scaleup.repository;

import com.teamsixnus.scaleup.domain.UserSkill;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long>, JpaSpecificationExecutor<UserSkill> {}
