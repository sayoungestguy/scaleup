package com.teamsixnus.scaleup.web.rest;

import static com.teamsixnus.scaleup.domain.ActivityAsserts.*;
import static com.teamsixnus.scaleup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsixnus.scaleup.IntegrationTest;
import com.teamsixnus.scaleup.domain.Activity;
import com.teamsixnus.scaleup.domain.Skill;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.repository.ActivityRepository;
import com.teamsixnus.scaleup.service.dto.ActivityDTO;
import com.teamsixnus.scaleup.service.mapper.ActivityMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivityResourceIT {

    private static final String DEFAULT_ACTIVITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTIVITY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVITY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;
    private static final Integer SMALLER_DURATION = 1 - 1;

    private static final String DEFAULT_VENUE = "AAAAAAAAAA";
    private static final String UPDATED_VENUE = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivityMockMvc;

    private Activity activity;

    private Activity insertedActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createEntity(EntityManager em) {
        Activity activity = new Activity()
            .activityName(DEFAULT_ACTIVITY_NAME)
            .activityTime(DEFAULT_ACTIVITY_TIME)
            .duration(DEFAULT_DURATION)
            .venue(DEFAULT_VENUE)
            .details(DEFAULT_DETAILS);
        return activity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createUpdatedEntity(EntityManager em) {
        Activity activity = new Activity()
            .activityName(UPDATED_ACTIVITY_NAME)
            .activityTime(UPDATED_ACTIVITY_TIME)
            .duration(UPDATED_DURATION)
            .venue(UPDATED_VENUE)
            .details(UPDATED_DETAILS);
        return activity;
    }

    @BeforeEach
    public void initTest() {
        activity = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedActivity != null) {
            activityRepository.delete(insertedActivity);
            insertedActivity = null;
        }
    }

    @Test
    @Transactional
    void createActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);
        var returnedActivityDTO = om.readValue(
            restActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ActivityDTO.class
        );

        // Validate the Activity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedActivity = activityMapper.toEntity(returnedActivityDTO);
        assertActivityUpdatableFieldsEquals(returnedActivity, getPersistedActivity(returnedActivity));

        insertedActivity = returnedActivity;
    }

    @Test
    @Transactional
    void createActivityWithExistingId() throws Exception {
        // Create the Activity with an existing ID
        activity.setId(1L);
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActivityTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setActivityTime(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActivities() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList
        restActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME)))
            .andExpect(jsonPath("$.[*].activityTime").value(hasItem(DEFAULT_ACTIVITY_TIME.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getActivity() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.activityName").value(DEFAULT_ACTIVITY_NAME))
            .andExpect(jsonPath("$.activityTime").value(DEFAULT_ACTIVITY_TIME.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.venue").value(DEFAULT_VENUE))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        Long id = activity.getId();

        defaultActivityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultActivityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultActivityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityName equals to
        defaultActivityFiltering("activityName.equals=" + DEFAULT_ACTIVITY_NAME, "activityName.equals=" + UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityName in
        defaultActivityFiltering(
            "activityName.in=" + DEFAULT_ACTIVITY_NAME + "," + UPDATED_ACTIVITY_NAME,
            "activityName.in=" + UPDATED_ACTIVITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityName is not null
        defaultActivityFiltering("activityName.specified=true", "activityName.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityName contains
        defaultActivityFiltering("activityName.contains=" + DEFAULT_ACTIVITY_NAME, "activityName.contains=" + UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityName does not contain
        defaultActivityFiltering(
            "activityName.doesNotContain=" + UPDATED_ACTIVITY_NAME,
            "activityName.doesNotContain=" + DEFAULT_ACTIVITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityTime equals to
        defaultActivityFiltering("activityTime.equals=" + DEFAULT_ACTIVITY_TIME, "activityTime.equals=" + UPDATED_ACTIVITY_TIME);
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityTime in
        defaultActivityFiltering(
            "activityTime.in=" + DEFAULT_ACTIVITY_TIME + "," + UPDATED_ACTIVITY_TIME,
            "activityTime.in=" + UPDATED_ACTIVITY_TIME
        );
    }

    @Test
    @Transactional
    void getAllActivitiesByActivityTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityTime is not null
        defaultActivityFiltering("activityTime.specified=true", "activityTime.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitiesByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where duration equals to
        defaultActivityFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllActivitiesByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where duration in
        defaultActivityFiltering("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION, "duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllActivitiesByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where duration is not null
        defaultActivityFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitiesByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where duration is greater than or equal to
        defaultActivityFiltering("duration.greaterThanOrEqual=" + DEFAULT_DURATION, "duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllActivitiesByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where duration is less than or equal to
        defaultActivityFiltering("duration.lessThanOrEqual=" + DEFAULT_DURATION, "duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllActivitiesByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where duration is less than
        defaultActivityFiltering("duration.lessThan=" + UPDATED_DURATION, "duration.lessThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllActivitiesByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where duration is greater than
        defaultActivityFiltering("duration.greaterThan=" + SMALLER_DURATION, "duration.greaterThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllActivitiesByVenueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where venue equals to
        defaultActivityFiltering("venue.equals=" + DEFAULT_VENUE, "venue.equals=" + UPDATED_VENUE);
    }

    @Test
    @Transactional
    void getAllActivitiesByVenueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where venue in
        defaultActivityFiltering("venue.in=" + DEFAULT_VENUE + "," + UPDATED_VENUE, "venue.in=" + UPDATED_VENUE);
    }

    @Test
    @Transactional
    void getAllActivitiesByVenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where venue is not null
        defaultActivityFiltering("venue.specified=true", "venue.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitiesByVenueContainsSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where venue contains
        defaultActivityFiltering("venue.contains=" + DEFAULT_VENUE, "venue.contains=" + UPDATED_VENUE);
    }

    @Test
    @Transactional
    void getAllActivitiesByVenueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList where venue does not contain
        defaultActivityFiltering("venue.doesNotContain=" + UPDATED_VENUE, "venue.doesNotContain=" + DEFAULT_VENUE);
    }

    @Test
    @Transactional
    void getAllActivitiesByCreatorProfileIsEqualToSomething() throws Exception {
        UserProfile creatorProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            activityRepository.saveAndFlush(activity);
            creatorProfile = UserProfileResourceIT.createEntity(em);
        } else {
            creatorProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(creatorProfile);
        em.flush();
        activity.setCreatorProfile(creatorProfile);
        activityRepository.saveAndFlush(activity);
        Long creatorProfileId = creatorProfile.getId();
        // Get all the activityList where creatorProfile equals to creatorProfileId
        defaultActivityShouldBeFound("creatorProfileId.equals=" + creatorProfileId);

        // Get all the activityList where creatorProfile equals to (creatorProfileId + 1)
        defaultActivityShouldNotBeFound("creatorProfileId.equals=" + (creatorProfileId + 1));
    }

    @Test
    @Transactional
    void getAllActivitiesBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            activityRepository.saveAndFlush(activity);
            skill = SkillResourceIT.createEntity(em);
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        activity.setSkill(skill);
        activityRepository.saveAndFlush(activity);
        Long skillId = skill.getId();
        // Get all the activityList where skill equals to skillId
        defaultActivityShouldBeFound("skillId.equals=" + skillId);

        // Get all the activityList where skill equals to (skillId + 1)
        defaultActivityShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    private void defaultActivityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultActivityShouldBeFound(shouldBeFound);
        defaultActivityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActivityShouldBeFound(String filter) throws Exception {
        restActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME)))
            .andExpect(jsonPath("$.[*].activityTime").value(hasItem(DEFAULT_ACTIVITY_TIME.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));

        // Check, that the count call also returns 1
        restActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActivityShouldNotBeFound(String filter) throws Exception {
        restActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivity() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity
        Activity updatedActivity = activityRepository.findById(activity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivity are not directly saved in db
        em.detach(updatedActivity);
        updatedActivity
            .activityName(UPDATED_ACTIVITY_NAME)
            .activityTime(UPDATED_ACTIVITY_TIME)
            .duration(UPDATED_DURATION)
            .venue(UPDATED_VENUE)
            .details(UPDATED_DETAILS);
        ActivityDTO activityDTO = activityMapper.toDto(updatedActivity);

        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivityToMatchAllProperties(updatedActivity);
    }

    @Test
    @Transactional
    void putNonExistingActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity.activityTime(UPDATED_ACTIVITY_TIME).duration(UPDATED_DURATION).details(UPDATED_DETAILS);

        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivity))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedActivity, activity), getPersistedActivity(activity));
    }

    @Test
    @Transactional
    void fullUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity
            .activityName(UPDATED_ACTIVITY_NAME)
            .activityTime(UPDATED_ACTIVITY_TIME)
            .duration(UPDATED_DURATION)
            .venue(UPDATED_VENUE)
            .details(UPDATED_DETAILS);

        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivity))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityUpdatableFieldsEquals(partialUpdatedActivity, getPersistedActivity(partialUpdatedActivity));
    }

    @Test
    @Transactional
    void patchNonExistingActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivity() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activity
        restActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, activity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activityRepository.count();
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

    protected Activity getPersistedActivity(Activity activity) {
        return activityRepository.findById(activity.getId()).orElseThrow();
    }

    protected void assertPersistedActivityToMatchAllProperties(Activity expectedActivity) {
        assertActivityAllPropertiesEquals(expectedActivity, getPersistedActivity(expectedActivity));
    }

    protected void assertPersistedActivityToMatchUpdatableProperties(Activity expectedActivity) {
        assertActivityAllUpdatablePropertiesEquals(expectedActivity, getPersistedActivity(expectedActivity));
    }
}
