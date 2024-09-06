package com.teamsixnus.scaleup.web.rest;

import static com.teamsixnus.scaleup.domain.CodeTablesAsserts.*;
import static com.teamsixnus.scaleup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsixnus.scaleup.IntegrationTest;
import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.repository.CodeTablesRepository;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import com.teamsixnus.scaleup.service.mapper.CodeTablesMapper;
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
 * Integration tests for the {@link CodeTablesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeTablesResourceIT {

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CODE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/code-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CodeTablesRepository codeTablesRepository;

    @Autowired
    private CodeTablesMapper codeTablesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeTablesMockMvc;

    private CodeTables codeTables;

    private CodeTables insertedCodeTables;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeTables createEntity(EntityManager em) {
        CodeTables codeTables = new CodeTables().category(DEFAULT_CATEGORY).codeKey(DEFAULT_CODE_KEY).codeValue(DEFAULT_CODE_VALUE);
        return codeTables;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeTables createUpdatedEntity(EntityManager em) {
        CodeTables codeTables = new CodeTables().category(UPDATED_CATEGORY).codeKey(UPDATED_CODE_KEY).codeValue(UPDATED_CODE_VALUE);
        return codeTables;
    }

    @BeforeEach
    public void initTest() {
        codeTables = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCodeTables != null) {
            codeTablesRepository.delete(insertedCodeTables);
            insertedCodeTables = null;
        }
    }

    @Test
    @Transactional
    void createCodeTables() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CodeTables
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);
        var returnedCodeTablesDTO = om.readValue(
            restCodeTablesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeTablesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CodeTablesDTO.class
        );

        // Validate the CodeTables in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCodeTables = codeTablesMapper.toEntity(returnedCodeTablesDTO);
        assertCodeTablesUpdatableFieldsEquals(returnedCodeTables, getPersistedCodeTables(returnedCodeTables));

        insertedCodeTables = returnedCodeTables;
    }

    @Test
    @Transactional
    void createCodeTablesWithExistingId() throws Exception {
        // Create the CodeTables with an existing ID
        codeTables.setId(1L);
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeTablesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeTablesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCodeTables() throws Exception {
        // Initialize the database
        insertedCodeTables = codeTablesRepository.saveAndFlush(codeTables);

        // Get all the codeTablesList
        restCodeTablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeTables.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].codeKey").value(hasItem(DEFAULT_CODE_KEY)))
            .andExpect(jsonPath("$.[*].codeValue").value(hasItem(DEFAULT_CODE_VALUE)));
    }

    @Test
    @Transactional
    void getCodeTables() throws Exception {
        // Initialize the database
        insertedCodeTables = codeTablesRepository.saveAndFlush(codeTables);

        // Get the codeTables
        restCodeTablesMockMvc
            .perform(get(ENTITY_API_URL_ID, codeTables.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeTables.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.codeKey").value(DEFAULT_CODE_KEY))
            .andExpect(jsonPath("$.codeValue").value(DEFAULT_CODE_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCodeTables() throws Exception {
        // Get the codeTables
        restCodeTablesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCodeTables() throws Exception {
        // Initialize the database
        insertedCodeTables = codeTablesRepository.saveAndFlush(codeTables);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeTables
        CodeTables updatedCodeTables = codeTablesRepository.findById(codeTables.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCodeTables are not directly saved in db
        em.detach(updatedCodeTables);
        updatedCodeTables.category(UPDATED_CATEGORY).codeKey(UPDATED_CODE_KEY).codeValue(UPDATED_CODE_VALUE);
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(updatedCodeTables);

        restCodeTablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeTablesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codeTablesDTO))
            )
            .andExpect(status().isOk());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCodeTablesToMatchAllProperties(updatedCodeTables);
    }

    @Test
    @Transactional
    void putNonExistingCodeTables() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeTables.setId(longCount.incrementAndGet());

        // Create the CodeTables
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeTablesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codeTablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeTables() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeTables.setId(longCount.incrementAndGet());

        // Create the CodeTables
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codeTablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeTables() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeTables.setId(longCount.incrementAndGet());

        // Create the CodeTables
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeTablesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeTablesWithPatch() throws Exception {
        // Initialize the database
        insertedCodeTables = codeTablesRepository.saveAndFlush(codeTables);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeTables using partial update
        CodeTables partialUpdatedCodeTables = new CodeTables();
        partialUpdatedCodeTables.setId(codeTables.getId());

        partialUpdatedCodeTables.codeKey(UPDATED_CODE_KEY).codeValue(UPDATED_CODE_VALUE);

        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeTables.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodeTables))
            )
            .andExpect(status().isOk());

        // Validate the CodeTables in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodeTablesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCodeTables, codeTables),
            getPersistedCodeTables(codeTables)
        );
    }

    @Test
    @Transactional
    void fullUpdateCodeTablesWithPatch() throws Exception {
        // Initialize the database
        insertedCodeTables = codeTablesRepository.saveAndFlush(codeTables);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeTables using partial update
        CodeTables partialUpdatedCodeTables = new CodeTables();
        partialUpdatedCodeTables.setId(codeTables.getId());

        partialUpdatedCodeTables.category(UPDATED_CATEGORY).codeKey(UPDATED_CODE_KEY).codeValue(UPDATED_CODE_VALUE);

        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeTables.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodeTables))
            )
            .andExpect(status().isOk());

        // Validate the CodeTables in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodeTablesUpdatableFieldsEquals(partialUpdatedCodeTables, getPersistedCodeTables(partialUpdatedCodeTables));
    }

    @Test
    @Transactional
    void patchNonExistingCodeTables() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeTables.setId(longCount.incrementAndGet());

        // Create the CodeTables
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeTablesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codeTablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeTables() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeTables.setId(longCount.incrementAndGet());

        // Create the CodeTables
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codeTablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeTables() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeTables.setId(longCount.incrementAndGet());

        // Create the CodeTables
        CodeTablesDTO codeTablesDTO = codeTablesMapper.toDto(codeTables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(codeTablesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeTables in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeTables() throws Exception {
        // Initialize the database
        insertedCodeTables = codeTablesRepository.saveAndFlush(codeTables);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the codeTables
        restCodeTablesMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeTables.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return codeTablesRepository.count();
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

    protected CodeTables getPersistedCodeTables(CodeTables codeTables) {
        return codeTablesRepository.findById(codeTables.getId()).orElseThrow();
    }

    protected void assertPersistedCodeTablesToMatchAllProperties(CodeTables expectedCodeTables) {
        assertCodeTablesAllPropertiesEquals(expectedCodeTables, getPersistedCodeTables(expectedCodeTables));
    }

    protected void assertPersistedCodeTablesToMatchUpdatableProperties(CodeTables expectedCodeTables) {
        assertCodeTablesAllUpdatablePropertiesEquals(expectedCodeTables, getPersistedCodeTables(expectedCodeTables));
    }
}
