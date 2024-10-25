package com.teamsixnus.scaleup.web.rest;

import static com.teamsixnus.scaleup.domain.ActivityInviteAsserts.*;
import static com.teamsixnus.scaleup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsixnus.scaleup.IntegrationTest;
import com.teamsixnus.scaleup.domain.*;
import com.teamsixnus.scaleup.repository.ActivityInviteRepository;
import com.teamsixnus.scaleup.security.AuthoritiesConstants;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import com.teamsixnus.scaleup.service.mapper.ActivityInviteMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ActivityInviteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivityInviteResourceIT {

    private static final Boolean DEFAULT_WILL_PARTICIPATE = false;
    private static final Boolean UPDATED_WILL_PARTICIPATE = true;

    private static final String ENTITY_API_URL = "/api/activity-invites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivityInviteRepository activityInviteRepository;

    @Autowired
    private ActivityInviteMapper activityInviteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivityInviteMockMvc;

    private ActivityInvite activityInvite;

    private ActivityInvite insertedActivityInvite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityInvite createEntity(EntityManager em) {
        ActivityInvite activityInvite = new ActivityInvite().willParticipate(DEFAULT_WILL_PARTICIPATE);
        return activityInvite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityInvite createUpdatedEntity(EntityManager em) {
        ActivityInvite activityInvite = new ActivityInvite().willParticipate(UPDATED_WILL_PARTICIPATE);
        return activityInvite;
    }

    @BeforeEach
    public void initTest() {
        activityInvite = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedActivityInvite != null) {
            activityInviteRepository.delete(insertedActivityInvite);
            insertedActivityInvite = null;
        }
    }

    @Test
    @Transactional
    void createActivityInvite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ActivityInvite
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);
        var returnedActivityInviteDTO = om.readValue(
            restActivityInviteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityInviteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ActivityInviteDTO.class
        );

        // Validate the ActivityInvite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedActivityInvite = activityInviteMapper.toEntity(returnedActivityInviteDTO);
        assertActivityInviteUpdatableFieldsEquals(returnedActivityInvite, getPersistedActivityInvite(returnedActivityInvite));

        insertedActivityInvite = returnedActivityInvite;
    }

    @Test
    @Transactional
    void createActivityInviteWithExistingId() throws Exception {
        // Create the ActivityInvite with an existing ID
        activityInvite.setId(1L);
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityInviteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityInviteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActivityInvites() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        // Get all the activityInviteList
        restActivityInviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityInvite.getId().intValue())))
            .andExpect(jsonPath("$.[*].willParticipate").value(hasItem(DEFAULT_WILL_PARTICIPATE.booleanValue())));
    }

    @Test
    @Transactional
    void getActivityInvite() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        // Get the activityInvite
        restActivityInviteMockMvc
            .perform(get(ENTITY_API_URL_ID, activityInvite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activityInvite.getId().intValue()))
            .andExpect(jsonPath("$.willParticipate").value(DEFAULT_WILL_PARTICIPATE.booleanValue()));
    }

    @Test
    @Transactional
    void getActivityInvitesByIdFiltering() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        Long id = activityInvite.getId();

        defaultActivityInviteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultActivityInviteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultActivityInviteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllActivityInvitesByWillParticipateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        // Get all the activityInviteList where willParticipate equals to
        defaultActivityInviteFiltering(
            "willParticipate.equals=" + DEFAULT_WILL_PARTICIPATE,
            "willParticipate.equals=" + UPDATED_WILL_PARTICIPATE
        );
    }

    @Test
    @Transactional
    void getAllActivityInvitesByWillParticipateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        // Get all the activityInviteList where willParticipate in
        defaultActivityInviteFiltering(
            "willParticipate.in=" + DEFAULT_WILL_PARTICIPATE + "," + UPDATED_WILL_PARTICIPATE,
            "willParticipate.in=" + UPDATED_WILL_PARTICIPATE
        );
    }

    @Test
    @Transactional
    void getAllActivityInvitesByWillParticipateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        // Get all the activityInviteList where willParticipate is not null
        defaultActivityInviteFiltering("willParticipate.specified=true", "willParticipate.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "user", authorities = { AuthoritiesConstants.USER })
    void getAllActivityInvitesByActivityIsEqualToSomething() throws Exception {
        Activity activity;
        if (TestUtil.findAll(em, Activity.class).isEmpty()) {
            activityInviteRepository.saveAndFlush(activityInvite);
            activity = ActivityResourceIT.createEntity(em);
        } else {
            activity = TestUtil.findAll(em, Activity.class).get(0);
        }
        em.persist(activity);
        em.flush();
        activityInvite.setActivity(activity);
        activityInviteRepository.saveAndFlush(activityInvite);
        Long activityId = activity.getId();
        // Get all the activityInviteList where activity equals to activityId
        defaultActivityInviteShouldBeFound("activityId.equals=" + activityId);

        // Get all the activityInviteList where activity equals to (activityId + 1)
        defaultActivityInviteShouldNotBeFound("activityId.equals=" + (activityId + 1));
    }

    @Test
    @Transactional
    void getAllActivityInvitesByInviteeProfileIsEqualToSomething() throws Exception {
        UserProfile inviteeProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            activityInviteRepository.saveAndFlush(activityInvite);
            inviteeProfile = UserProfileResourceIT.createEntity(em);
        } else {
            inviteeProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(inviteeProfile);
        em.flush();
        activityInvite.setInviteeProfile(inviteeProfile);
        activityInviteRepository.saveAndFlush(activityInvite);
        Long inviteeProfileId = inviteeProfile.getId();
        // Get all the activityInviteList where inviteeProfile equals to inviteeProfileId
        defaultActivityInviteShouldBeFound("inviteeProfileId.equals=" + inviteeProfileId);

        // Get all the activityInviteList where inviteeProfile equals to (inviteeProfileId + 1)
        defaultActivityInviteShouldNotBeFound("inviteeProfileId.equals=" + (inviteeProfileId + 1));
    }

    @Test
    @Transactional
    void getAllActivityInvitesByStatusIsEqualToSomething() throws Exception {
        CodeTables status;
        if (TestUtil.findAll(em, CodeTables.class).isEmpty()) {
            activityInviteRepository.saveAndFlush(activityInvite);
            status = CodeTablesResourceIT.createEntity(em);
        } else {
            status = TestUtil.findAll(em, CodeTables.class).get(0);
        }
        em.persist(status);
        em.flush();
        activityInvite.setStatus(status);
        activityInviteRepository.saveAndFlush(activityInvite);
        Long statusId = status.getId();
        // Get all the activityInviteList where status equals to statusId
        defaultActivityInviteShouldBeFound("statusId.equals=" + statusId);

        // Get all the activityInviteList where status equals to (statusId + 1)
        defaultActivityInviteShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultActivityInviteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultActivityInviteShouldBeFound(shouldBeFound);
        defaultActivityInviteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActivityInviteShouldBeFound(String filter) throws Exception {
        restActivityInviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityInvite.getId().intValue())))
            .andExpect(jsonPath("$.[*].willParticipate").value(hasItem(DEFAULT_WILL_PARTICIPATE.booleanValue())));

        // Check, that the count call also returns 1
        restActivityInviteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActivityInviteShouldNotBeFound(String filter) throws Exception {
        restActivityInviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActivityInviteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingActivityInvite() throws Exception {
        // Get the activityInvite
        restActivityInviteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivityInvite() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityInvite
        ActivityInvite updatedActivityInvite = activityInviteRepository.findById(activityInvite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivityInvite are not directly saved in db
        em.detach(updatedActivityInvite);
        updatedActivityInvite.willParticipate(UPDATED_WILL_PARTICIPATE);
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(updatedActivityInvite);

        restActivityInviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityInviteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityInviteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivityInviteToMatchAllProperties(updatedActivityInvite);
    }

    @Test
    @Transactional
    void putNonExistingActivityInvite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityInvite.setId(longCount.incrementAndGet());

        // Create the ActivityInvite
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityInviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityInviteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivityInvite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityInvite.setId(longCount.incrementAndGet());

        // Create the ActivityInvite
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityInviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivityInvite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityInvite.setId(longCount.incrementAndGet());

        // Create the ActivityInvite
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityInviteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityInviteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivityInviteWithPatch() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityInvite using partial update
        ActivityInvite partialUpdatedActivityInvite = new ActivityInvite();
        partialUpdatedActivityInvite.setId(activityInvite.getId());

        restActivityInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivityInvite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivityInvite))
            )
            .andExpect(status().isOk());

        // Validate the ActivityInvite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityInviteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedActivityInvite, activityInvite),
            getPersistedActivityInvite(activityInvite)
        );
    }

    @Test
    @Transactional
    void fullUpdateActivityInviteWithPatch() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityInvite using partial update
        ActivityInvite partialUpdatedActivityInvite = new ActivityInvite();
        partialUpdatedActivityInvite.setId(activityInvite.getId());

        partialUpdatedActivityInvite.willParticipate(UPDATED_WILL_PARTICIPATE);

        restActivityInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivityInvite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivityInvite))
            )
            .andExpect(status().isOk());

        // Validate the ActivityInvite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityInviteUpdatableFieldsEquals(partialUpdatedActivityInvite, getPersistedActivityInvite(partialUpdatedActivityInvite));
    }

    @Test
    @Transactional
    void patchNonExistingActivityInvite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityInvite.setId(longCount.incrementAndGet());

        // Create the ActivityInvite
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activityInviteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivityInvite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityInvite.setId(longCount.incrementAndGet());

        // Create the ActivityInvite
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivityInvite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityInvite.setId(longCount.incrementAndGet());

        // Create the ActivityInvite
        ActivityInviteDTO activityInviteDTO = activityInviteMapper.toDto(activityInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityInviteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activityInviteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivityInvite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivityInvite() throws Exception {
        // Initialize the database
        insertedActivityInvite = activityInviteRepository.saveAndFlush(activityInvite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activityInvite
        restActivityInviteMockMvc
            .perform(delete(ENTITY_API_URL_ID, activityInvite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activityInviteRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ActivityInvite getPersistedActivityInvite(ActivityInvite activityInvite) {
        return activityInviteRepository.findById(activityInvite.getId()).orElseThrow();
    }

    protected void assertPersistedActivityInviteToMatchAllProperties(ActivityInvite expectedActivityInvite) {
        assertActivityInviteAllPropertiesEquals(expectedActivityInvite, getPersistedActivityInvite(expectedActivityInvite));
    }

    protected void assertPersistedActivityInviteToMatchUpdatableProperties(ActivityInvite expectedActivityInvite) {
        assertActivityInviteAllUpdatablePropertiesEquals(expectedActivityInvite, getPersistedActivityInvite(expectedActivityInvite));
    }

    // Mock helper function to return the current user
    private User mockCurrentUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setLogin(username);
        return user;
    }
}
