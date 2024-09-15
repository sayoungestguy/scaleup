package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.*; // for static metamodels
import com.teamsixnus.scaleup.domain.UserSkill;
import com.teamsixnus.scaleup.repository.UserSkillRepository;
import com.teamsixnus.scaleup.service.criteria.UserSkillCriteria;
import com.teamsixnus.scaleup.service.dto.UserSkillDTO;
import com.teamsixnus.scaleup.service.mapper.UserSkillMapper;
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
 * Service for executing complex queries for {@link UserSkill} entities in the database.
 * The main input is a {@link UserSkillCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserSkillDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserSkillQueryService extends QueryService<UserSkill> {

    private static final Logger log = LoggerFactory.getLogger(UserSkillQueryService.class);

    private final UserSkillRepository userSkillRepository;

    private final UserSkillMapper userSkillMapper;

    public UserSkillQueryService(UserSkillRepository userSkillRepository, UserSkillMapper userSkillMapper) {
        this.userSkillRepository = userSkillRepository;
        this.userSkillMapper = userSkillMapper;
    }

    /**
     * Return a {@link Page} of {@link UserSkillDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserSkillDTO> findByCriteria(UserSkillCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserSkill> specification = createSpecification(criteria);
        return userSkillRepository.findAll(specification, page).map(userSkillMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserSkillCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserSkill> specification = createSpecification(criteria);
        return userSkillRepository.count(specification);
    }

    /**
     * Function to convert {@link UserSkillCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserSkill> createSpecification(UserSkillCriteria criteria) {
        Specification<UserSkill> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserSkill_.id));
            }
            if (criteria.getYearsOfExperience() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYearsOfExperience(), UserSkill_.yearsOfExperience));
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getUserProfileId(),
                        root -> root.join(UserSkill_.userProfile, JoinType.LEFT).get(UserProfile_.id)
                    )
                );
            }
            if (criteria.getSkillId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSkillId(), root -> root.join(UserSkill_.skill, JoinType.LEFT).get(Skill_.id))
                );
            }
            if (criteria.getSkillTypeId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getSkillTypeId(),
                        root -> root.join(UserSkill_.skillType, JoinType.LEFT).get(CodeTables_.id)
                    )
                );
            }
        }
        return specification;
    }
}
