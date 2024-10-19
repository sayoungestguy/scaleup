package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.*; // for static metamodels
import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.repository.CodeTablesRepository;
import com.teamsixnus.scaleup.service.criteria.CodeTablesCriteria;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import com.teamsixnus.scaleup.service.mapper.CodeTablesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CodeTables} entities in the database.
 * The main input is a {@link CodeTablesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CodeTablesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CodeTablesQueryService extends QueryService<CodeTables> {

    private static final Logger log = LoggerFactory.getLogger(CodeTablesQueryService.class);

    private final CodeTablesRepository codeTablesRepository;

    private final CodeTablesMapper codeTablesMapper;

    public CodeTablesQueryService(CodeTablesRepository codeTablesRepository, CodeTablesMapper codeTablesMapper) {
        this.codeTablesRepository = codeTablesRepository;
        this.codeTablesMapper = codeTablesMapper;
    }

    /**
     * Return a {@link Page} of {@link CodeTablesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CodeTablesDTO> findByCriteria(CodeTablesCriteria criteria, Pageable page) {
        // log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CodeTables> specification = createSpecification(criteria);
        return codeTablesRepository.findAll(specification, page).map(codeTablesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CodeTablesCriteria criteria) {
        // log.debug("count by criteria : {}", criteria);
        final Specification<CodeTables> specification = createSpecification(criteria);
        return codeTablesRepository.count(specification);
    }

    /**
     * Function to convert {@link CodeTablesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CodeTables> createSpecification(CodeTablesCriteria criteria) {
        Specification<CodeTables> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CodeTables_.id));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), CodeTables_.category));
            }
            if (criteria.getCodeKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeKey(), CodeTables_.codeKey));
            }
            if (criteria.getCodeValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeValue(), CodeTables_.codeValue));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), CodeTables_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), CodeTables_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), CodeTables_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), CodeTables_.lastModifiedDate));
            }
        }
        return specification;
    }
}
