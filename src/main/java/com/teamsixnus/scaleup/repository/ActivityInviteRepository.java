package com.teamsixnus.scaleup.repository;

import com.teamsixnus.scaleup.domain.ActivityInvite;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import java.util.List;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ActivityInvite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityInviteRepository extends JpaRepository<ActivityInvite, Long>, JpaSpecificationExecutor<ActivityInvite> {
    /**
     * Find all activities by the activity's ID.
     *
     * @param activityId the ID of the activity.
     * @return a list of activities created by the given activity id.
     */
    List<ActivityInvite> findByActivityId(Long activityId);
}
