package com.teamsixnus.scaleup.web.rest;

import static com.teamsixnus.scaleup.domain.UserSkillAsserts.*;
import static com.teamsixnus.scaleup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsixnus.scaleup.IntegrationTest;
import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.domain.Skill;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.domain.UserSkill;
import com.teamsixnus.scaleup.repository.UserSkillRepository;
import com.teamsixnus.scaleup.service.dto.UserSkillDTO;
import com.teamsixnus.scaleup.service.mapper.UserSkillMapper;
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
 * Integration tests for the {@link UserSkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserSkillResourceIT {

    private static final Integer DEFAULT_YEARS_OF_EXPERIENCE = 1;
    private static final Integer UPDATED_YEARS_OF_EXPERIENCE = 2;
    private static final Integer SMALLER_YEARS_OF_EXPERIENCE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/user-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private UserSkillMapper userSkillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSkillMockMvc;

    private UserSkill userSkill;

    private UserSkill insertedUserSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSkill createEntity(EntityManager em) {
        UserSkill userSkill = new UserSkill().yearsOfExperience(DEFAULT_YEARS_OF_EXPERIENCE);
        return userSkill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSkill createUpdatedEntity(EntityManager em) {
        UserSkill userSkill = new UserSkill().yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE);
        return userSkill;
    }

    @BeforeEach
    public void initTest() {
        userSkill = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserSkill != null) {
            userSkillRepository.delete(insertedUserSkill);
            insertedUserSkill = null;
        }
    }

    @Test
    @Transactional
    void createUserSkill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);
        var returnedUserSkillDTO = om.readValue(
            restUserSkillMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSkillDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserSkillDTO.class
        );

        // Validate the UserSkill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserSkill = userSkillMapper.toEntity(returnedUserSkillDTO);
        assertUserSkillUpdatableFieldsEquals(returnedUserSkill, getPersistedUserSkill(returnedUserSkill));

        insertedUserSkill = returnedUserSkill;
    }

    @Test
    @Transactional
    void createUserSkillWithExistingId() throws Exception {
        // Create the UserSkill with an existing ID
        userSkill.setId(1L);
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSkillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkYearsOfExperienceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSkill.setYearsOfExperience(null);

        // Create the UserSkill, which fails.
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        restUserSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSkillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserSkills() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList
        restUserSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)));
    }

    @Test
    @Transactional
    void getUserSkill() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get the userSkill
        restUserSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, userSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSkill.getId().intValue()))
            .andExpect(jsonPath("$.yearsOfExperience").value(DEFAULT_YEARS_OF_EXPERIENCE));
    }

    @Test
    @Transactional
    void getUserSkillsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        Long id = userSkill.getId();

        defaultUserSkillFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserSkillFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserSkillFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserSkillsByYearsOfExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList where yearsOfExperience equals to
        defaultUserSkillFiltering(
            "yearsOfExperience.equals=" + DEFAULT_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.equals=" + UPDATED_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllUserSkillsByYearsOfExperienceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList where yearsOfExperience in
        defaultUserSkillFiltering(
            "yearsOfExperience.in=" + DEFAULT_YEARS_OF_EXPERIENCE + "," + UPDATED_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.in=" + UPDATED_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllUserSkillsByYearsOfExperienceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList where yearsOfExperience is not null
        defaultUserSkillFiltering("yearsOfExperience.specified=true", "yearsOfExperience.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSkillsByYearsOfExperienceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList where yearsOfExperience is greater than or equal to
        defaultUserSkillFiltering(
            "yearsOfExperience.greaterThanOrEqual=" + DEFAULT_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.greaterThanOrEqual=" + UPDATED_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllUserSkillsByYearsOfExperienceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList where yearsOfExperience is less than or equal to
        defaultUserSkillFiltering(
            "yearsOfExperience.lessThanOrEqual=" + DEFAULT_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.lessThanOrEqual=" + SMALLER_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllUserSkillsByYearsOfExperienceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList where yearsOfExperience is less than
        defaultUserSkillFiltering(
            "yearsOfExperience.lessThan=" + UPDATED_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.lessThan=" + DEFAULT_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllUserSkillsByYearsOfExperienceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        // Get all the userSkillList where yearsOfExperience is greater than
        defaultUserSkillFiltering(
            "yearsOfExperience.greaterThan=" + SMALLER_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.greaterThan=" + DEFAULT_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllUserSkillsByUserProfileIsEqualToSomething() throws Exception {
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userSkillRepository.saveAndFlush(userSkill);
            userProfile = UserProfileResourceIT.createEntity(em);
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(userProfile);
        em.flush();
        userSkill.setUserProfile(userProfile);
        userSkillRepository.saveAndFlush(userSkill);
        Long userProfileId = userProfile.getId();
        // Get all the userSkillList where userProfile equals to userProfileId
        defaultUserSkillShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the userSkillList where userProfile equals to (userProfileId + 1)
        defaultUserSkillShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    @Test
    @Transactional
    void getAllUserSkillsBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            userSkillRepository.saveAndFlush(userSkill);
            skill = SkillResourceIT.createEntity(em);
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        userSkill.setSkill(skill);
        userSkillRepository.saveAndFlush(userSkill);
        Long skillId = skill.getId();
        // Get all the userSkillList where skill equals to skillId
        defaultUserSkillShouldBeFound("skillId.equals=" + skillId);

        // Get all the userSkillList where skill equals to (skillId + 1)
        defaultUserSkillShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    @Test
    @Transactional
    void getAllUserSkillsBySkillTypeIsEqualToSomething() throws Exception {
        CodeTables skillType;
        if (TestUtil.findAll(em, CodeTables.class).isEmpty()) {
            userSkillRepository.saveAndFlush(userSkill);
            skillType = CodeTablesResourceIT.createEntity(em);
        } else {
            skillType = TestUtil.findAll(em, CodeTables.class).get(0);
        }
        em.persist(skillType);
        em.flush();
        userSkill.setSkillType(skillType);
        userSkillRepository.saveAndFlush(userSkill);
        Long skillTypeId = skillType.getId();
        // Get all the userSkillList where skillType equals to skillTypeId
        defaultUserSkillShouldBeFound("skillTypeId.equals=" + skillTypeId);

        // Get all the userSkillList where skillType equals to (skillTypeId + 1)
        defaultUserSkillShouldNotBeFound("skillTypeId.equals=" + (skillTypeId + 1));
    }

    private void defaultUserSkillFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserSkillShouldBeFound(shouldBeFound);
        defaultUserSkillShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserSkillShouldBeFound(String filter) throws Exception {
        restUserSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)));

        // Check, that the count call also returns 1
        restUserSkillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserSkillShouldNotBeFound(String filter) throws Exception {
        restUserSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserSkillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserSkill() throws Exception {
        // Get the userSkill
        restUserSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSkill() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSkill
        UserSkill updatedUserSkill = userSkillRepository.findById(userSkill.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserSkill are not directly saved in db
        em.detach(updatedUserSkill);
        updatedUserSkill.yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE);
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(updatedUserSkill);

        restUserSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSkillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSkillDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserSkillToMatchAllProperties(updatedUserSkill);
    }

    @Test
    @Transactional
    void putNonExistingUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSkillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSkillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSkillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSkillWithPatch() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSkill using partial update
        UserSkill partialUpdatedUserSkill = new UserSkill();
        partialUpdatedUserSkill.setId(userSkill.getId());

        restUserSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSkill))
            )
            .andExpect(status().isOk());

        // Validate the UserSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSkillUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserSkill, userSkill),
            getPersistedUserSkill(userSkill)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserSkillWithPatch() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSkill using partial update
        UserSkill partialUpdatedUserSkill = new UserSkill();
        partialUpdatedUserSkill.setId(userSkill.getId());

        partialUpdatedUserSkill.yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE);

        restUserSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSkill))
            )
            .andExpect(status().isOk());

        // Validate the UserSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSkillUpdatableFieldsEquals(partialUpdatedUserSkill, getPersistedUserSkill(partialUpdatedUserSkill));
    }

    @Test
    @Transactional
    void patchNonExistingUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSkillDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSkillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userSkillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSkill() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.saveAndFlush(userSkill);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userSkill
        restUserSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSkill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userSkillRepository.count();
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

    protected UserSkill getPersistedUserSkill(UserSkill userSkill) {
        return userSkillRepository.findById(userSkill.getId()).orElseThrow();
    }

    protected void assertPersistedUserSkillToMatchAllProperties(UserSkill expectedUserSkill) {
        assertUserSkillAllPropertiesEquals(expectedUserSkill, getPersistedUserSkill(expectedUserSkill));
    }

    protected void assertPersistedUserSkillToMatchUpdatableProperties(UserSkill expectedUserSkill) {
        assertUserSkillAllUpdatablePropertiesEquals(expectedUserSkill, getPersistedUserSkill(expectedUserSkill));
    }
}
