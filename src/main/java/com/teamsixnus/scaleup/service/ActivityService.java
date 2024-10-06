package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.Activity;
import com.teamsixnus.scaleup.domain.User;
import com.teamsixnus.scaleup.repository.ActivityRepository;
import com.teamsixnus.scaleup.security.AuthoritiesConstants;
import com.teamsixnus.scaleup.service.dto.ActivityDTO;
import com.teamsixnus.scaleup.service.mapper.ActivityMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserService userService;

    public ActivityService(ActivityRepository activityRepository, ActivityMapper activityMapper, UserService userService) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
        this.userService = userService;
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

    //    /**
    //     * Get all the activities.
    //     *
    //     * @param pageable the pagination information.
    //     * @return the list of entities.
    //     */
    //    @Transactional(readOnly = true)
    //    public Page<ActivityDTO> findAll(Pageable pageable) {
    //        log.debug("Request to get all Activities");
    //        Optional<User> currentUser = userService.getUserById();
    //
    //        if (currentUser.isPresent()) {
    //            if (currentUser.get().getAuthorities().stream().anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN))) {
    //                return activityRepository.findAll(pageable).map(activityMapper::toDto);
    //            } else {
    //                return activityRepository.findAllByCreatorProfileUserId(currentUser.get().getId(), pageable).map(activityMapper::toDto);
    //            }
    //        }
    //        return Page.empty(); // Or throw an exception if user not found
    //    }

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

    @Transactional(readOnly = true)
    public Page<ActivityDTO> findAllByCurrentUser(Pageable pageable) {
        User currentUser = userService.getUserById().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return activityRepository.findAllByCreatorProfileUserId(currentUser.getId(), pageable).map(activityMapper::toDto);
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
