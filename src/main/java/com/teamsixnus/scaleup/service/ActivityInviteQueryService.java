package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.*; // for static metamodels
import com.teamsixnus.scaleup.domain.ActivityInvite;
import com.teamsixnus.scaleup.repository.ActivityInviteRepository;
import com.teamsixnus.scaleup.service.criteria.ActivityInviteCriteria;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import com.teamsixnus.scaleup.service.mapper.ActivityInviteMapper;
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
 * Service for executing complex queries for {@link ActivityInvite} entities in the database.
 * The main input is a {@link ActivityInviteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ActivityInviteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActivityInviteQueryService extends QueryService<ActivityInvite> {

    private static final Logger log = LoggerFactory.getLogger(ActivityInviteQueryService.class);

    private final ActivityInviteRepository activityInviteRepository;

    private final ActivityInviteMapper activityInviteMapper;

    public ActivityInviteQueryService(ActivityInviteRepository activityInviteRepository, ActivityInviteMapper activityInviteMapper) {
        this.activityInviteRepository = activityInviteRepository;
        this.activityInviteMapper = activityInviteMapper;
    }

    /**
     * Return a {@link Page} of {@link ActivityInviteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActivityInviteDTO> findByCriteria(ActivityInviteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ActivityInvite> specification = createSpecification(criteria);
        return activityInviteRepository.findAll(specification, page).map(activityInviteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActivityInviteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ActivityInvite> specification = createSpecification(criteria);
        return activityInviteRepository.count(specification);
    }

    /**
     * Function to convert {@link ActivityInviteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ActivityInvite> createSpecification(ActivityInviteCriteria criteria) {
        Specification<ActivityInvite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ActivityInvite_.id));
            }
            if (criteria.getWillParticipate() != null) {
                specification = specification.and(buildSpecification(criteria.getWillParticipate(), ActivityInvite_.willParticipate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ActivityInvite_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ActivityInvite_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ActivityInvite_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), ActivityInvite_.lastModifiedDate)
                );
            }
            if (criteria.getActivityId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getActivityId(),
                        root -> root.join(ActivityInvite_.activity, JoinType.LEFT).get(Activity_.id)
                    )
                );
            }
            if (criteria.getInviteeProfileId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getInviteeProfileId(),
                        root -> root.join(ActivityInvite_.inviteeProfile, JoinType.LEFT).get(UserProfile_.id)
                    )
                );
            }
            if (criteria.getStatusId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStatusId(), root -> root.join(ActivityInvite_.status, JoinType.LEFT).get(CodeTables_.id))
                );
            }
        }
        return specification;
    }
}
