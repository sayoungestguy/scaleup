package com.teamsixnus.scaleup.repository;

import com.teamsixnus.scaleup.domain.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByCreatorProfileUserId(Long userId, Pageable pageable);
}
