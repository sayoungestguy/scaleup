package com.teamsixnus.scaleup.web.rest;

import static com.teamsixnus.scaleup.domain.SkillAsserts.*;
import static com.teamsixnus.scaleup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsixnus.scaleup.IntegrationTest;
import com.teamsixnus.scaleup.domain.Skill;
import com.teamsixnus.scaleup.repository.SkillRepository;
import com.teamsixnus.scaleup.service.dto.SkillDTO;
import com.teamsixnus.scaleup.service.mapper.SkillMapper;
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
 * Integration tests for the {@link SkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SkillResourceIT {

    private static final String DEFAULT_SKILL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SKILL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INDIVIDUAL_SKILL_DESC = "AAAAAAAAAA";
    private static final String UPDATED_INDIVIDUAL_SKILL_DESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEARS_OF_EXP = 1;
    private static final Integer UPDATED_YEARS_OF_EXP = 2;

    private static final String ENTITY_API_URL = "/api/skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillMockMvc;

    private Skill skill;

    private Skill insertedSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createEntity(EntityManager em) {
        Skill skill = new Skill()
            .skillName(DEFAULT_SKILL_NAME)
            .individualSkillDesc(DEFAULT_INDIVIDUAL_SKILL_DESC)
            .yearsOfExp(DEFAULT_YEARS_OF_EXP);
        return skill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createUpdatedEntity(EntityManager em) {
        Skill skill = new Skill()
            .skillName(UPDATED_SKILL_NAME)
            .individualSkillDesc(UPDATED_INDIVIDUAL_SKILL_DESC)
            .yearsOfExp(UPDATED_YEARS_OF_EXP);
        return skill;
    }

    @BeforeEach
    public void initTest() {
        skill = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSkill != null) {
            skillRepository.delete(insertedSkill);
            insertedSkill = null;
        }
    }

    @Test
    @Transactional
    void createSkill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);
        var returnedSkillDTO = om.readValue(
            restSkillMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SkillDTO.class
        );

        // Validate the Skill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSkill = skillMapper.toEntity(returnedSkillDTO);
        assertSkillUpdatableFieldsEquals(returnedSkill, getPersistedSkill(returnedSkill));

        insertedSkill = returnedSkill;
    }

    @Test
    @Transactional
    void createSkillWithExistingId() throws Exception {
        // Create the Skill with an existing ID
        skill.setId(1L);
        SkillDTO skillDTO = skillMapper.toDto(skill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkillNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setSkillName(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearsOfExpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setYearsOfExp(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSkills() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList
        restSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skill.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillName").value(hasItem(DEFAULT_SKILL_NAME)))
            .andExpect(jsonPath("$.[*].individualSkillDesc").value(hasItem(DEFAULT_INDIVIDUAL_SKILL_DESC)))
            .andExpect(jsonPath("$.[*].yearsOfExp").value(hasItem(DEFAULT_YEARS_OF_EXP)));
    }

    @Test
    @Transactional
    void getSkill() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get the skill
        restSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, skill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skill.getId().intValue()))
            .andExpect(jsonPath("$.skillName").value(DEFAULT_SKILL_NAME))
            .andExpect(jsonPath("$.individualSkillDesc").value(DEFAULT_INDIVIDUAL_SKILL_DESC))
            .andExpect(jsonPath("$.yearsOfExp").value(DEFAULT_YEARS_OF_EXP));
    }

    @Test
    @Transactional
    void getNonExistingSkill() throws Exception {
        // Get the skill
        restSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkill() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill
        Skill updatedSkill = skillRepository.findById(skill.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSkill are not directly saved in db
        em.detach(updatedSkill);
        updatedSkill.skillName(UPDATED_SKILL_NAME).individualSkillDesc(UPDATED_INDIVIDUAL_SKILL_DESC).yearsOfExp(UPDATED_YEARS_OF_EXP);
        SkillDTO skillDTO = skillMapper.toDto(updatedSkill);

        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillToMatchAllProperties(updatedSkill);
    }

    @Test
    @Transactional
    void putNonExistingSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.skillName(UPDATED_SKILL_NAME).yearsOfExp(UPDATED_YEARS_OF_EXP);

        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkill))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSkill, skill), getPersistedSkill(skill));
    }

    @Test
    @Transactional
    void fullUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill
            .skillName(UPDATED_SKILL_NAME)
            .individualSkillDesc(UPDATED_INDIVIDUAL_SKILL_DESC)
            .yearsOfExp(UPDATED_YEARS_OF_EXP);

        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkill))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpdatableFieldsEquals(partialUpdatedSkill, getPersistedSkill(partialUpdatedSkill));
    }

    @Test
    @Transactional
    void patchNonExistingSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkill() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skill
        restSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, skill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillRepository.count();
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

    protected Skill getPersistedSkill(Skill skill) {
        return skillRepository.findById(skill.getId()).orElseThrow();
    }

    protected void assertPersistedSkillToMatchAllProperties(Skill expectedSkill) {
        assertSkillAllPropertiesEquals(expectedSkill, getPersistedSkill(expectedSkill));
    }

    protected void assertPersistedSkillToMatchUpdatableProperties(Skill expectedSkill) {
        assertSkillAllUpdatablePropertiesEquals(expectedSkill, getPersistedSkill(expectedSkill));
    }
}
