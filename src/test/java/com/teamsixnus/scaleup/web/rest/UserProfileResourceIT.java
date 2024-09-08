//package com.teamsixnus.scaleup.web.rest;
//
//import static com.teamsixnus.scaleup.domain.UserProfileAsserts.*;
//import static com.teamsixnus.scaleup.web.rest.TestUtil.createUpdateProxyForBean;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.teamsixnus.scaleup.IntegrationTest;
//import com.teamsixnus.scaleup.domain.User;
//import com.teamsixnus.scaleup.domain.UserProfile;
//import com.teamsixnus.scaleup.repository.UserProfileRepository;
//import com.teamsixnus.scaleup.repository.UserRepository;
//import com.teamsixnus.scaleup.service.UserProfileService;
//import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
//import com.teamsixnus.scaleup.service.mapper.UserProfileMapper;
//import jakarta.persistence.EntityManager;
//import java.util.ArrayList;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link UserProfileResource} REST controller.
// */
//@IntegrationTest
//@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc
//@WithMockUser
//class UserProfileResourceIT {
//
//    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
//    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_JOB_ROLE = "AAAAAAAAAA";
//    private static final String UPDATED_JOB_ROLE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_ABOUT_ME = "AAAAAAAAAA";
//    private static final String UPDATED_ABOUT_ME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PROFILE_PICTURE = "AAAAAAAAAA";
//    private static final String UPDATED_PROFILE_PICTURE = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/user-profiles";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private ObjectMapper om;
//
//    @Autowired
//    private UserProfileRepository userProfileRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Mock
//    private UserProfileRepository userProfileRepositoryMock;
//
//    @Autowired
//    private UserProfileMapper userProfileMapper;
//
//    @Mock
//    private UserProfileService userProfileServiceMock;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restUserProfileMockMvc;
//
//    private UserProfile userProfile;
//
//    private UserProfile insertedUserProfile;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static UserProfile createEntity(EntityManager em) {
//        UserProfile userProfile = new UserProfile()
//            .nickname(DEFAULT_NICKNAME)
//            .jobRole(DEFAULT_JOB_ROLE)
//            .aboutMe(DEFAULT_ABOUT_ME)
//            .profilePicture(DEFAULT_PROFILE_PICTURE);
//        // Add required entity
//        User user = UserResourceIT.createEntity(em);
//        em.persist(user);
//        em.flush();
//        userProfile.setUser(user);
//        return userProfile;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static UserProfile createUpdatedEntity(EntityManager em) {
//        UserProfile userProfile = new UserProfile()
//            .nickname(UPDATED_NICKNAME)
//            .jobRole(UPDATED_JOB_ROLE)
//            .aboutMe(UPDATED_ABOUT_ME)
//            .profilePicture(UPDATED_PROFILE_PICTURE);
//        // Add required entity
//        User user = UserResourceIT.createEntity(em);
//        em.persist(user);
//        em.flush();
//        userProfile.setUser(user);
//        return userProfile;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        userProfile = createEntity(em);
//    }
//
//    @AfterEach
//    public void cleanup() {
//        if (insertedUserProfile != null) {
//            userProfileRepository.delete(insertedUserProfile);
//            insertedUserProfile = null;
//        }
//    }
//
//    @Test
//    @Transactional
//    void createUserProfile() throws Exception {
//        long databaseSizeBeforeCreate = getRepositoryCount();
//        // Create the UserProfile
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//        var returnedUserProfileDTO = om.readValue(
//            restUserProfileMockMvc
//                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(),
//            UserProfileDTO.class
//        );
//
//        // Validate the UserProfile in the database
//        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
//        var returnedUserProfile = userProfileMapper.toEntity(returnedUserProfileDTO);
//        assertUserProfileUpdatableFieldsEquals(returnedUserProfile, getPersistedUserProfile(returnedUserProfile));
//
//        insertedUserProfile = returnedUserProfile;
//    }
//
//    @Test
//    @Transactional
//    void createUserProfileWithExistingId() throws Exception {
//        // Create the UserProfile with an existing ID
//        userProfile.setId(1L);
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//
//        long databaseSizeBeforeCreate = getRepositoryCount();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restUserProfileMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfiles() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList
//        restUserProfileMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
//            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
//            .andExpect(jsonPath("$.[*].jobRole").value(hasItem(DEFAULT_JOB_ROLE)))
//            .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME)))
//            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(DEFAULT_PROFILE_PICTURE)));
//    }
//
//    @SuppressWarnings({ "unchecked" })
//    void getAllUserProfilesWithEagerRelationshipsIsEnabled() throws Exception {
//        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//
//        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());
//
//        verify(userProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
//    }
//
//    @SuppressWarnings({ "unchecked" })
//    void getAllUserProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
//        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//
//        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
//        verify(userProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
//    }
//
//    @Test
//    @Transactional
//    void getUserProfile() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get the userProfile
//        restUserProfileMockMvc
//            .perform(get(ENTITY_API_URL_ID, userProfile.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
//            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME))
//            .andExpect(jsonPath("$.jobRole").value(DEFAULT_JOB_ROLE))
//            .andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME))
//            .andExpect(jsonPath("$.profilePicture").value(DEFAULT_PROFILE_PICTURE));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingUserProfile() throws Exception {
//        // Get the userProfile
//        restUserProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putExistingUserProfile() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//
//        // Update the userProfile
//        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
//        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
//        em.detach(updatedUserProfile);
//        updatedUserProfile
//            .nickname(UPDATED_NICKNAME)
//            .jobRole(UPDATED_JOB_ROLE)
//            .aboutMe(UPDATED_ABOUT_ME)
//            .profilePicture(UPDATED_PROFILE_PICTURE);
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);
//
//        restUserProfileMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, userProfileDTO.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(om.writeValueAsBytes(userProfileDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//        assertPersistedUserProfileToMatchAllProperties(updatedUserProfile);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingUserProfile() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        userProfile.setId(longCount.incrementAndGet());
//
//        // Create the UserProfile
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restUserProfileMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, userProfileDTO.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(om.writeValueAsBytes(userProfileDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchUserProfile() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        userProfile.setId(longCount.incrementAndGet());
//
//        // Create the UserProfile
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserProfileMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(om.writeValueAsBytes(userProfileDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamUserProfile() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        userProfile.setId(longCount.incrementAndGet());
//
//        // Create the UserProfile
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserProfileMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateUserProfileWithPatch() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//
//        // Update the userProfile using partial update
//        UserProfile partialUpdatedUserProfile = new UserProfile();
//        partialUpdatedUserProfile.setId(userProfile.getId());
//
//        partialUpdatedUserProfile.nickname(UPDATED_NICKNAME).jobRole(UPDATED_JOB_ROLE).profilePicture(UPDATED_PROFILE_PICTURE);
//
//        restUserProfileMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(partialUpdatedUserProfile))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the UserProfile in the database
//
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//        assertUserProfileUpdatableFieldsEquals(
//            createUpdateProxyForBean(partialUpdatedUserProfile, userProfile),
//            getPersistedUserProfile(userProfile)
//        );
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateUserProfileWithPatch() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//
//        // Update the userProfile using partial update
//        UserProfile partialUpdatedUserProfile = new UserProfile();
//        partialUpdatedUserProfile.setId(userProfile.getId());
//
//        partialUpdatedUserProfile
//            .nickname(UPDATED_NICKNAME)
//            .jobRole(UPDATED_JOB_ROLE)
//            .aboutMe(UPDATED_ABOUT_ME)
//            .profilePicture(UPDATED_PROFILE_PICTURE);
//
//        restUserProfileMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(partialUpdatedUserProfile))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the UserProfile in the database
//
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//        assertUserProfileUpdatableFieldsEquals(partialUpdatedUserProfile, getPersistedUserProfile(partialUpdatedUserProfile));
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingUserProfile() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        userProfile.setId(longCount.incrementAndGet());
//
//        // Create the UserProfile
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restUserProfileMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, userProfileDTO.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(userProfileDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchUserProfile() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        userProfile.setId(longCount.incrementAndGet());
//
//        // Create the UserProfile
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserProfileMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(userProfileDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamUserProfile() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        userProfile.setId(longCount.incrementAndGet());
//
//        // Create the UserProfile
//        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserProfileMockMvc
//            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userProfileDTO)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the UserProfile in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteUserProfile() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        long databaseSizeBeforeDelete = getRepositoryCount();
//
//        // Delete the userProfile
//        restUserProfileMockMvc
//            .perform(delete(ENTITY_API_URL_ID, userProfile.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
//    }
//
//    protected long getRepositoryCount() {
//        return userProfileRepository.count();
//    }
//
//    protected void assertIncrementedRepositoryCount(long countBefore) {
//        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
//    }
//
//    protected void assertDecrementedRepositoryCount(long countBefore) {
//        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
//    }
//
//    protected void assertSameRepositoryCount(long countBefore) {
//        assertThat(countBefore).isEqualTo(getRepositoryCount());
//    }
//
//    protected UserProfile getPersistedUserProfile(UserProfile userProfile) {
//        return userProfileRepository.findById(userProfile.getId()).orElseThrow();
//    }
//
//    protected void assertPersistedUserProfileToMatchAllProperties(UserProfile expectedUserProfile) {
//        assertUserProfileAllPropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
//    }
//
//    protected void assertPersistedUserProfileToMatchUpdatableProperties(UserProfile expectedUserProfile) {
//        assertUserProfileAllUpdatablePropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
//    }
//}
