package com.teamsixnus.scaleup.web.rest;

import com.teamsixnus.scaleup.repository.ActivityInviteRepository;
import com.teamsixnus.scaleup.service.ActivityInviteQueryService;
import com.teamsixnus.scaleup.service.ActivityInviteService;
import com.teamsixnus.scaleup.service.criteria.ActivityInviteCriteria;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import com.teamsixnus.scaleup.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.teamsixnus.scaleup.domain.ActivityInvite}.
 */
@RestController
@RequestMapping("/api/activity-invites")
public class ActivityInviteResource {

    private static final Logger log = LoggerFactory.getLogger(ActivityInviteResource.class);

    private static final String ENTITY_NAME = "activityInvite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityInviteService activityInviteService;

    private final ActivityInviteRepository activityInviteRepository;

    private final ActivityInviteQueryService activityInviteQueryService;

    public ActivityInviteResource(
        ActivityInviteService activityInviteService,
        ActivityInviteRepository activityInviteRepository,
        ActivityInviteQueryService activityInviteQueryService
    ) {
        this.activityInviteService = activityInviteService;
        this.activityInviteRepository = activityInviteRepository;
        this.activityInviteQueryService = activityInviteQueryService;
    }

    /**
     * {@code POST  /activity-invites} : Create a new activityInvite.
     *
     * @param activityInviteDTO the activityInviteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityInviteDTO, or with status {@code 400 (Bad Request)} if the activityInvite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ActivityInviteDTO> createActivityInvite(@RequestBody ActivityInviteDTO activityInviteDTO)
        throws URISyntaxException {
        log.debug("REST request to save ActivityInvite : {}", activityInviteDTO);
        if (activityInviteDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityInvite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activityInviteDTO = activityInviteService.save(activityInviteDTO);
        return ResponseEntity.created(new URI("/api/activity-invites/" + activityInviteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, activityInviteDTO.getId().toString()))
            .body(activityInviteDTO);
    }

    /**
     * {@code PUT  /activity-invites/:id} : Updates an existing activityInvite.
     *
     * @param id the id of the activityInviteDTO to save.
     * @param activityInviteDTO the activityInviteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityInviteDTO,
     * or with status {@code 400 (Bad Request)} if the activityInviteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityInviteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActivityInviteDTO> updateActivityInvite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActivityInviteDTO activityInviteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ActivityInvite : {}, {}", id, activityInviteDTO);
        if (activityInviteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityInviteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityInviteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activityInviteDTO = activityInviteService.update(activityInviteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activityInviteDTO.getId().toString()))
            .body(activityInviteDTO);
    }

    /**
     * {@code PATCH  /activity-invites/:id} : Partial updates given fields of an existing activityInvite, field will ignore if it is null
     *
     * @param id the id of the activityInviteDTO to save.
     * @param activityInviteDTO the activityInviteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityInviteDTO,
     * or with status {@code 400 (Bad Request)} if the activityInviteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the activityInviteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the activityInviteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActivityInviteDTO> partialUpdateActivityInvite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActivityInviteDTO activityInviteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActivityInvite partially : {}, {}", id, activityInviteDTO);
        if (activityInviteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityInviteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityInviteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActivityInviteDTO> result = activityInviteService.partialUpdate(activityInviteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activityInviteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /activity-invites} : get all the activityInvites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityInvites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ActivityInviteDTO>> getAllActivityInvites(
        ActivityInviteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ActivityInvites by criteria: {}", criteria);

        Page<ActivityInviteDTO> page = activityInviteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /activity-invites/count} : count all the activityInvites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countActivityInvites(ActivityInviteCriteria criteria) {
        log.debug("REST request to count ActivityInvites by criteria: {}", criteria);
        return ResponseEntity.ok().body(activityInviteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /activity-invites/:id} : get the "id" activityInvite.
     *
     * @param id the id of the activityInviteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityInviteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActivityInviteDTO> getActivityInvite(@PathVariable("id") Long id) {
        log.debug("REST request to get ActivityInvite : {}", id);
        Optional<ActivityInviteDTO> activityInviteDTO = activityInviteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityInviteDTO);
    }

    /**
     * {@code DELETE  /activity-invites/:id} : delete the "id" activityInvite.
     *
     * @param id the id of the activityInviteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityInvite(@PathVariable("id") Long id) {
        log.debug("REST request to delete ActivityInvite : {}", id);
        activityInviteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
