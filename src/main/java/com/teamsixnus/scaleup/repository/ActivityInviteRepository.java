package com.teamsixnus.scaleup.repository;

import com.teamsixnus.scaleup.domain.ActivityInvite;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ActivityInvite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityInviteRepository extends JpaRepository<ActivityInvite, Long> {
    // Fetch all invites for a specific activity by its ID
    List<ActivityInviteDTO> findByActivity_Id(Long activityId);
}
