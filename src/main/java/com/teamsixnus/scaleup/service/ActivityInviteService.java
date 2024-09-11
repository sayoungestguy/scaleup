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

    // Fetch all invites for a specific activity
    public List<ActivityInviteDTO> getInvitesByActivityId(Long activityId) {
        return activityInviteRepository.findByActivity_Id(activityId);
    }
}
