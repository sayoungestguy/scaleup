package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.Activity;
import com.teamsixnus.scaleup.repository.ActivityRepository;
import com.teamsixnus.scaleup.service.dto.ActivityDTO;
import com.teamsixnus.scaleup.service.mapper.ActivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teamsixnus.scaleup.domain.Activity}.
 */
@Service
@Transactional
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityService(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    public ActivityDTO save(ActivityDTO activityDTO) {
        log.debug("Request to save Activity : {}", activityDTO);
        Activity activity = activityMapper.toEntity(activityDTO);
        activity = activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    /**
     * Update a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    public ActivityDTO update(ActivityDTO activityDTO) {
        log.debug("Request to update Activity : {}", activityDTO);
        Activity activity = activityMapper.toEntity(activityDTO);
        activity.setIsPersisted();
        activity = activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    /**
     * Partially update a activity.
     *
     * @param activityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActivityDTO> partialUpdate(ActivityDTO activityDTO) {
        log.debug("Request to partially update Activity : {}", activityDTO);

        return activityRepository
            .findById(activityDTO.getId())
            .map(existingActivity -> {
                activityMapper.partialUpdate(existingActivity, activityDTO);

                return existingActivity;
            })
            .map(activityRepository::save)
            .map(activityMapper::toDto);
    }

    /**
     * Get all the activities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActivityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Activities");
        return activityRepository.findAll(pageable).map(activityMapper::toDto);
    }

    /**
     * Get one activity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActivityDTO> findOne(Long id) {
        log.debug("Request to get Activity : {}", id);
        return activityRepository.findById(id).map(activityMapper::toDto);
    }

    /**
     * Delete the activity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Activity : {}", id);
        activityRepository.deleteById(id);
    }
}
