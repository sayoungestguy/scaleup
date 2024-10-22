package com.teamsixnus.scaleup.web.rest;

import com.teamsixnus.scaleup.repository.UserSkillRepository;
import com.teamsixnus.scaleup.service.UserSkillQueryService;
import com.teamsixnus.scaleup.service.UserSkillService;
import com.teamsixnus.scaleup.service.criteria.UserSkillCriteria;
import com.teamsixnus.scaleup.service.dto.UserSkillDTO;
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
 * REST controller for managing {@link com.teamsixnus.scaleup.domain.UserSkill}.
 */
@RestController
@RequestMapping("/api/user-skills")
public class UserSkillResource {

    private static final Logger log = LoggerFactory.getLogger(UserSkillResource.class);

    private static final String ENTITY_NAME = "userSkill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSkillService userSkillService;

    private final UserSkillRepository userSkillRepository;

    private final UserSkillQueryService userSkillQueryService;

    public UserSkillResource(
        UserSkillService userSkillService,
        UserSkillRepository userSkillRepository,
        UserSkillQueryService userSkillQueryService
    ) {
        this.userSkillService = userSkillService;
        this.userSkillRepository = userSkillRepository;
        this.userSkillQueryService = userSkillQueryService;
    }

    /**
     * {@code POST  /user-skills} : Create a new userSkill.
     *
     * @param userSkillDTO the userSkillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSkillDTO, or with status {@code 400 (Bad Request)} if the userSkill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserSkillDTO> createUserSkill(@Valid @RequestBody UserSkillDTO userSkillDTO) throws URISyntaxException {
        log.debug("REST request to save UserSkill : {}", userSkillDTO);
        if (userSkillDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userSkillDTO = userSkillService.save(userSkillDTO);
        return ResponseEntity.created(new URI("/api/user-skills/" + userSkillDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userSkillDTO.getId().toString()))
            .body(userSkillDTO);
    }

    /**
     * {@code PUT  /user-skills/:id} : Updates an existing userSkill.
     *
     * @param id the id of the userSkillDTO to save.
     * @param userSkillDTO the userSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSkillDTO,
     * or with status {@code 400 (Bad Request)} if the userSkillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSkillDTO> updateUserSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserSkillDTO userSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserSkill : {}, {}", id, userSkillDTO);
        if (userSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userSkillDTO = userSkillService.update(userSkillDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSkillDTO.getId().toString()))
            .body(userSkillDTO);
    }

    /**
     * {@code PATCH  /user-skills/:id} : Partial updates given fields of an existing userSkill, field will ignore if it is null
     *
     * @param id the id of the userSkillDTO to save.
     * @param userSkillDTO the userSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSkillDTO,
     * or with status {@code 400 (Bad Request)} if the userSkillDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userSkillDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSkillDTO> partialUpdateUserSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserSkillDTO userSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserSkill partially : {}, {}", id, userSkillDTO);
        if (userSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSkillDTO> result = userSkillService.partialUpdate(userSkillDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSkillDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-skills} : get all the userSkills.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSkills in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserSkillDTO>> getAllUserSkills(
        UserSkillCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        // log.debug("REST request to get UserSkills by criteria: {}", criteria);

        Page<UserSkillDTO> page = userSkillQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-skills/count} : count all the userSkills.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserSkills(UserSkillCriteria criteria) {
        // log.debug("REST request to count UserSkills by criteria: {}", criteria);
        return ResponseEntity.ok().body(userSkillQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-skills/:id} : get the "id" userSkill.
     *
     * @param id the id of the userSkillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSkillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSkillDTO> getUserSkill(@PathVariable("id") Long id) {
        log.debug("REST request to get UserSkill : {}", id);
        Optional<UserSkillDTO> userSkillDTO = userSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSkillDTO);
    }

    /**
     * {@code DELETE  /user-skills/:id} : delete the "id" userSkill.
     *
     * @param id the id of the userSkillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSkill(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserSkill : {}", id);
        userSkillService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
