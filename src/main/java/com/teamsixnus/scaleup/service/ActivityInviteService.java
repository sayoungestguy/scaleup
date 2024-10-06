package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.ActivityInvite;
import com.teamsixnus.scaleup.repository.ActivityInviteRepository;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import com.teamsixnus.scaleup.service.mapper.ActivityInviteMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teamsixnus.scaleup.domain.ActivityInvite}.
 */
@Service
@Transactional
public class ActivityInviteService {

    private static final Logger log = LoggerFactory.getLogger(ActivityInviteService.class);

    private final ActivityInviteRepository activityInviteRepository;

    private final ActivityInviteMapper activityInviteMapper;

    public ActivityInviteService(ActivityInviteRepository activityInviteRepository, ActivityInviteMapper activityInviteMapper) {
        this.activityInviteRepository = activityInviteRepository;
        this.activityInviteMapper = activityInviteMapper;
    }

    /**
     * Save a activityInvite.
     *
     * @param activityInviteDTO the entity to save.
     * @return the persisted entity.
     */
    public ActivityInviteDTO save(ActivityInviteDTO activityInviteDTO) {
        log.debug("Request to save ActivityInvite : {}", activityInviteDTO);
        ActivityInvite activityInvite = activityInviteMapper.toEntity(activityInviteDTO);
        activityInvite = activityInviteRepository.save(activityInvite);
        return activityInviteMapper.toDto(activityInvite);
    }

    /**
     * Update a activityInvite.
     *
     * @param activityInviteDTO the entity to save.
     * @return the persisted entity.
     */
    public ActivityInviteDTO update(ActivityInviteDTO activityInviteDTO) {
        log.debug("Request to update ActivityInvite : {}", activityInviteDTO);
        ActivityInvite activityInvite = activityInviteMapper.toEntity(activityInviteDTO);
        activityInvite.setIsPersisted();
        activityInvite = activityInviteRepository.save(activityInvite);
        return activityInviteMapper.toDto(activityInvite);
    }

    /**
     * Partially update a activityInvite.
     *
     * @param activityInviteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActivityInviteDTO> partialUpdate(ActivityInviteDTO activityInviteDTO) {
        log.debug("Request to partially update ActivityInvite : {}", activityInviteDTO);

        return activityInviteRepository
            .findById(activityInviteDTO.getId())
            .map(existingActivityInvite -> {
                activityInviteMapper.partialUpdate(existingActivityInvite, activityInviteDTO);

                return existingActivityInvite;
            })
            .map(activityInviteRepository::save)
            .map(activityInviteMapper::toDto);
    }

    /**
     * Get one activityInvite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActivityInviteDTO> findOne(Long id) {
        log.debug("Request to get ActivityInvite : {}", id);
        return activityInviteRepository.findById(id).map(activityInviteMapper::toDto);
    }

    /**
     * Delete the activityInvite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivityInvite : {}", id);
        activityInviteRepository.deleteById(id);
    }

    /**
     * Delete all activity invites linked to a specific activity ID.
     *
     * @param activityId the ID of the activity whose invites need to be deleted.
     * @return true if all invites were deleted, false if there was an issue.
     */
    public boolean deleteAllInvitesByActivityId(Long activityId) {
        // Step 1: Retrieve all activity invites linked to the activity ID
        List<ActivityInvite> invitesToDelete = activityInviteRepository.findByActivityId(activityId);

        // Step 2: Check if the list is empty
        if (invitesToDelete.isEmpty()) {
            // No invites found, return true (nothing to delete)
            return true;
        }

        // Step 3: Loop through the list and delete each invite one by one
        try {
            for (ActivityInvite invite : invitesToDelete) {
                activityInviteRepository.delete(invite);
            }
            return true; // Successfully deleted all invites
        } catch (Exception e) {
            // If there's any issue with deletion, log it and return false
            log.error("Error deleting activity invites for activity ID: {}", activityId, e);
            return false;
        }
    }
}
