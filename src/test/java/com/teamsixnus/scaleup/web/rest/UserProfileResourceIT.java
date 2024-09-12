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
//    private static final String DEFAULT_SOCIAL_LINKS = "AAAAAAAAAA";
//    private static final String UPDATED_SOCIAL_LINKS = "BBBBBBBBBB";
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
//            .profilePicture(DEFAULT_PROFILE_PICTURE)
//            .socialLinks(DEFAULT_SOCIAL_LINKS);
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
//            .profilePicture(UPDATED_PROFILE_PICTURE)
//            .socialLinks(UPDATED_SOCIAL_LINKS);
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
//            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(DEFAULT_PROFILE_PICTURE)))
//            .andExpect(jsonPath("$.[*].socialLinks").value(hasItem(DEFAULT_SOCIAL_LINKS)));
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
//            .andExpect(jsonPath("$.profilePicture").value(DEFAULT_PROFILE_PICTURE))
//            .andExpect(jsonPath("$.socialLinks").value(DEFAULT_SOCIAL_LINKS));
//    }
//
//    @Test
//    @Transactional
//    void getUserProfilesByIdFiltering() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        Long id = userProfile.getId();
//
//        defaultUserProfileFiltering("id.equals=" + id, "id.notEquals=" + id);
//
//        defaultUserProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);
//
//        defaultUserProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByNicknameIsEqualToSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where nickname equals to
//        defaultUserProfileFiltering("nickname.equals=" + DEFAULT_NICKNAME, "nickname.equals=" + UPDATED_NICKNAME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByNicknameIsInShouldWork() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where nickname in
//        defaultUserProfileFiltering("nickname.in=" + DEFAULT_NICKNAME + "," + UPDATED_NICKNAME, "nickname.in=" + UPDATED_NICKNAME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByNicknameIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where nickname is not null
//        defaultUserProfileFiltering("nickname.specified=true", "nickname.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByNicknameContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where nickname contains
//        defaultUserProfileFiltering("nickname.contains=" + DEFAULT_NICKNAME, "nickname.contains=" + UPDATED_NICKNAME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByNicknameNotContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where nickname does not contain
//        defaultUserProfileFiltering("nickname.doesNotContain=" + UPDATED_NICKNAME, "nickname.doesNotContain=" + DEFAULT_NICKNAME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByJobRoleIsEqualToSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where jobRole equals to
//        defaultUserProfileFiltering("jobRole.equals=" + DEFAULT_JOB_ROLE, "jobRole.equals=" + UPDATED_JOB_ROLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByJobRoleIsInShouldWork() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where jobRole in
//        defaultUserProfileFiltering("jobRole.in=" + DEFAULT_JOB_ROLE + "," + UPDATED_JOB_ROLE, "jobRole.in=" + UPDATED_JOB_ROLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByJobRoleIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where jobRole is not null
//        defaultUserProfileFiltering("jobRole.specified=true", "jobRole.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByJobRoleContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where jobRole contains
//        defaultUserProfileFiltering("jobRole.contains=" + DEFAULT_JOB_ROLE, "jobRole.contains=" + UPDATED_JOB_ROLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByJobRoleNotContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where jobRole does not contain
//        defaultUserProfileFiltering("jobRole.doesNotContain=" + UPDATED_JOB_ROLE, "jobRole.doesNotContain=" + DEFAULT_JOB_ROLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByAboutMeIsEqualToSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where aboutMe equals to
//        defaultUserProfileFiltering("aboutMe.equals=" + DEFAULT_ABOUT_ME, "aboutMe.equals=" + UPDATED_ABOUT_ME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByAboutMeIsInShouldWork() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where aboutMe in
//        defaultUserProfileFiltering("aboutMe.in=" + DEFAULT_ABOUT_ME + "," + UPDATED_ABOUT_ME, "aboutMe.in=" + UPDATED_ABOUT_ME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByAboutMeIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where aboutMe is not null
//        defaultUserProfileFiltering("aboutMe.specified=true", "aboutMe.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByAboutMeContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where aboutMe contains
//        defaultUserProfileFiltering("aboutMe.contains=" + DEFAULT_ABOUT_ME, "aboutMe.contains=" + UPDATED_ABOUT_ME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByAboutMeNotContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where aboutMe does not contain
//        defaultUserProfileFiltering("aboutMe.doesNotContain=" + UPDATED_ABOUT_ME, "aboutMe.doesNotContain=" + DEFAULT_ABOUT_ME);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByProfilePictureIsEqualToSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where profilePicture equals to
//        defaultUserProfileFiltering("profilePicture.equals=" + DEFAULT_PROFILE_PICTURE, "profilePicture.equals=" + UPDATED_PROFILE_PICTURE);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByProfilePictureIsInShouldWork() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where profilePicture in
//        defaultUserProfileFiltering(
//            "profilePicture.in=" + DEFAULT_PROFILE_PICTURE + "," + UPDATED_PROFILE_PICTURE,
//            "profilePicture.in=" + UPDATED_PROFILE_PICTURE
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByProfilePictureIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where profilePicture is not null
//        defaultUserProfileFiltering("profilePicture.specified=true", "profilePicture.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByProfilePictureContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where profilePicture contains
//        defaultUserProfileFiltering(
//            "profilePicture.contains=" + DEFAULT_PROFILE_PICTURE,
//            "profilePicture.contains=" + UPDATED_PROFILE_PICTURE
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByProfilePictureNotContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where profilePicture does not contain
//        defaultUserProfileFiltering(
//            "profilePicture.doesNotContain=" + UPDATED_PROFILE_PICTURE,
//            "profilePicture.doesNotContain=" + DEFAULT_PROFILE_PICTURE
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesBySocialLinksIsEqualToSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where socialLinks equals to
//        defaultUserProfileFiltering("socialLinks.equals=" + DEFAULT_SOCIAL_LINKS, "socialLinks.equals=" + UPDATED_SOCIAL_LINKS);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesBySocialLinksIsInShouldWork() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where socialLinks in
//        defaultUserProfileFiltering(
//            "socialLinks.in=" + DEFAULT_SOCIAL_LINKS + "," + UPDATED_SOCIAL_LINKS,
//            "socialLinks.in=" + UPDATED_SOCIAL_LINKS
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesBySocialLinksIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where socialLinks is not null
//        defaultUserProfileFiltering("socialLinks.specified=true", "socialLinks.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesBySocialLinksContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where socialLinks contains
//        defaultUserProfileFiltering("socialLinks.contains=" + DEFAULT_SOCIAL_LINKS, "socialLinks.contains=" + UPDATED_SOCIAL_LINKS);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesBySocialLinksNotContainsSomething() throws Exception {
//        // Initialize the database
//        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where socialLinks does not contain
//        defaultUserProfileFiltering(
//            "socialLinks.doesNotContain=" + UPDATED_SOCIAL_LINKS,
//            "socialLinks.doesNotContain=" + DEFAULT_SOCIAL_LINKS
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllUserProfilesByUserIsEqualToSomething() throws Exception {
//        // Get already existing entity
//        User user = userProfile.getUser();
//        userProfileRepository.saveAndFlush(userProfile);
//        Long userId = user.getId();
//        // Get all the userProfileList where user equals to userId
//        defaultUserProfileShouldBeFound("userId.equals=" + userId);
//
//        // Get all the userProfileList where user equals to (userId + 1)
//        defaultUserProfileShouldNotBeFound("userId.equals=" + (userId + 1));
//    }
//
//    private void defaultUserProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
//        defaultUserProfileShouldBeFound(shouldBeFound);
//        defaultUserProfileShouldNotBeFound(shouldNotBeFound);
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned.
//     */
//    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
//        restUserProfileMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
//            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
//            .andExpect(jsonPath("$.[*].jobRole").value(hasItem(DEFAULT_JOB_ROLE)))
//            .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME)))
//            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(DEFAULT_PROFILE_PICTURE)))
//            .andExpect(jsonPath("$.[*].socialLinks").value(hasItem(DEFAULT_SOCIAL_LINKS)));
//
//        // Check, that the count call also returns 1
//        restUserProfileMockMvc
//            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned.
//     */
//    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
//        restUserProfileMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restUserProfileMockMvc
//            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("0"));
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
//            .profilePicture(UPDATED_PROFILE_PICTURE)
//            .socialLinks(UPDATED_SOCIAL_LINKS);
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
//            .profilePicture(UPDATED_PROFILE_PICTURE)
//            .socialLinks(UPDATED_SOCIAL_LINKS);
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
