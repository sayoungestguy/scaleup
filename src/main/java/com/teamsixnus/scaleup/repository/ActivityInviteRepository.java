package com.teamsixnus.scaleup.repository;

import com.teamsixnus.scaleup.domain.ActivityInvite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ActivityInvite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityInviteRepository extends JpaRepository<ActivityInvite, Long> {}
