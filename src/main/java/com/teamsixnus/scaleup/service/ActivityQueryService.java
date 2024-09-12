package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.*; // for static metamodels
import com.teamsixnus.scaleup.domain.Activity;
import com.teamsixnus.scaleup.repository.ActivityRepository;
import com.teamsixnus.scaleup.service.criteria.ActivityCriteria;
import com.teamsixnus.scaleup.service.dto.ActivityDTO;
import com.teamsixnus.scaleup.service.mapper.ActivityMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Activity} entities in the database.
 * The main input is a {@link ActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActivityQueryService extends QueryService<Activity> {

    private static final Logger log = LoggerFactory.getLogger(ActivityQueryService.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityQueryService(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    /**
     * Return a {@link Page} of {@link ActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActivityDTO> findByCriteria(ActivityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Activity> specification = createSpecification(criteria);
        return activityRepository.findAll(specification, page).map(activityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActivityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Activity> specification = createSpecification(criteria);
        return activityRepository.count(specification);
    }

    /**
     * Function to convert {@link ActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Activity> createSpecification(ActivityCriteria criteria) {
        Specification<Activity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Activity_.id));
            }
            if (criteria.getActivityName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActivityName(), Activity_.activityName));
            }
            if (criteria.getActivityTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivityTime(), Activity_.activityTime));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), Activity_.duration));
            }
            if (criteria.getVenue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVenue(), Activity_.venue));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Activity_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Activity_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Activity_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Activity_.lastModifiedDate));
            }
            if (criteria.getCreatorProfileId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCreatorProfileId(),
                        root -> root.join(Activity_.creatorProfile, JoinType.LEFT).get(UserProfile_.id)
                    )
                );
            }
            if (criteria.getSkillId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSkillId(), root -> root.join(Activity_.skill, JoinType.LEFT).get(Skill_.id))
                );
            }
        }
        return specification;
    }
}
