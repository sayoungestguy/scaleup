package com.teamsixnus.scaleup.web.rest;

import com.teamsixnus.scaleup.repository.CodeTablesRepository;
import com.teamsixnus.scaleup.service.CodeTablesQueryService;
import com.teamsixnus.scaleup.service.CodeTablesService;
import com.teamsixnus.scaleup.service.criteria.CodeTablesCriteria;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import com.teamsixnus.scaleup.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.teamsixnus.scaleup.domain.CodeTables}.
 */
@RestController
@RequestMapping("/api/code-tables")
public class CodeTablesResource {

    private static final Logger log = LoggerFactory.getLogger(CodeTablesResource.class);

    private static final String ENTITY_NAME = "codeTables";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodeTablesService codeTablesService;

    private final CodeTablesRepository codeTablesRepository;

    private final CodeTablesQueryService codeTablesQueryService;

    public CodeTablesResource(
        CodeTablesService codeTablesService,
        CodeTablesRepository codeTablesRepository,
        CodeTablesQueryService codeTablesQueryService
    ) {
        this.codeTablesService = codeTablesService;
        this.codeTablesRepository = codeTablesRepository;
        this.codeTablesQueryService = codeTablesQueryService;
    }

    /**
     * {@code POST  /code-tables} : Create a new codeTables.
     *
     * @param codeTablesDTO the codeTablesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeTablesDTO, or with status {@code 400 (Bad Request)} if the codeTables has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CodeTablesDTO> createCodeTables(@Valid @RequestBody CodeTablesDTO codeTablesDTO) throws URISyntaxException {
        log.debug("REST request to save CodeTables : {}", codeTablesDTO);
        if (codeTablesDTO.getId() != null) {
            throw new BadRequestAlertException("A new codeTables cannot already have an ID", ENTITY_NAME, "idexists");
        }
        codeTablesDTO = codeTablesService.save(codeTablesDTO);
        return ResponseEntity.created(new URI("/api/code-tables/" + codeTablesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, codeTablesDTO.getId().toString()))
            .body(codeTablesDTO);
    }

    /**
     * {@code PUT  /code-tables/:id} : Updates an existing codeTables.
     *
     * @param id the id of the codeTablesDTO to save.
     * @param codeTablesDTO the codeTablesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeTablesDTO,
     * or with status {@code 400 (Bad Request)} if the codeTablesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeTablesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodeTablesDTO> updateCodeTables(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CodeTablesDTO codeTablesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CodeTables : {}, {}", id, codeTablesDTO);
        if (codeTablesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeTablesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeTablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        codeTablesDTO = codeTablesService.update(codeTablesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, codeTablesDTO.getId().toString()))
            .body(codeTablesDTO);
    }

    /**
     * {@code PATCH  /code-tables/:id} : Partial updates given fields of an existing codeTables, field will ignore if it is null
     *
     * @param id the id of the codeTablesDTO to save.
     * @param codeTablesDTO the codeTablesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeTablesDTO,
     * or with status {@code 400 (Bad Request)} if the codeTablesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the codeTablesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeTablesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodeTablesDTO> partialUpdateCodeTables(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CodeTablesDTO codeTablesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CodeTables partially : {}, {}", id, codeTablesDTO);
        if (codeTablesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeTablesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeTablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeTablesDTO> result = codeTablesService.partialUpdate(codeTablesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, codeTablesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /code-tables} : get all the codeTables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeTables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CodeTablesDTO>> getAllCodeTables(
        CodeTablesCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CodeTables by criteria: {}", criteria);

        Page<CodeTablesDTO> page = codeTablesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /code-tables/count} : count all the codeTables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCodeTables(CodeTablesCriteria criteria) {
        log.debug("REST request to count CodeTables by criteria: {}", criteria);
        return ResponseEntity.ok().body(codeTablesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /code-tables/:id} : get the "id" codeTables.
     *
     * @param id the id of the codeTablesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeTablesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodeTablesDTO> getCodeTables(@PathVariable("id") Long id) {
        log.debug("REST request to get CodeTables : {}", id);
        Optional<CodeTablesDTO> codeTablesDTO = codeTablesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codeTablesDTO);
    }

    /**
     * {@code DELETE  /code-tables/:id} : delete the "id" codeTables.
     *
     * @param id the id of the codeTablesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodeTables(@PathVariable("id") Long id) {
        log.debug("REST request to delete CodeTables : {}", id);
        codeTablesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
