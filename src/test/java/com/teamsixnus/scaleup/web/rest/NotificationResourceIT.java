//package com.teamsixnus.scaleup.web.rest;
//
//import static com.teamsixnus.scaleup.domain.NotificationAsserts.*;
//import static com.teamsixnus.scaleup.web.rest.TestUtil.createUpdateProxyForBean;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.teamsixnus.scaleup.IntegrationTest;
//import com.teamsixnus.scaleup.domain.CodeTables;
//import com.teamsixnus.scaleup.domain.Notification;
//import com.teamsixnus.scaleup.domain.UserProfile;
//import com.teamsixnus.scaleup.repository.NotificationRepository;
//import com.teamsixnus.scaleup.service.dto.NotificationDTO;
//import com.teamsixnus.scaleup.service.mapper.NotificationMapper;
//import jakarta.persistence.EntityManager;
//import java.util.Random;
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicLong;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link NotificationResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class NotificationResourceIT {
//
//    private static final UUID DEFAULT_NOTIFICATION_REF_ID = UUID.randomUUID();
//    private static final UUID UPDATED_NOTIFICATION_REF_ID = UUID.randomUUID();
//
//    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
//    private static final String UPDATED_CONTENT = "BBBBBBBBBB";
//
//    private static final Boolean DEFAULT_IS_READ = false;
//    private static final Boolean UPDATED_IS_READ = true;
//
//    private static final String ENTITY_API_URL = "/api/notifications";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private ObjectMapper om;
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private NotificationMapper notificationMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restNotificationMockMvc;
//
//    private Notification notification;
//
//    private Notification insertedNotification;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Notification createEntity(EntityManager em) {
//        Notification notification = new Notification()
//            .notificationRefId(DEFAULT_NOTIFICATION_REF_ID)
//            .content(DEFAULT_CONTENT)
//            .isRead(DEFAULT_IS_READ);
//        return notification;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Notification createUpdatedEntity(EntityManager em) {
//        Notification notification = new Notification()
//            .notificationRefId(UPDATED_NOTIFICATION_REF_ID)
//            .content(UPDATED_CONTENT)
//            .isRead(UPDATED_IS_READ);
//        return notification;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        notification = createEntity(em);
//    }
//
//    @AfterEach
//    public void cleanup() {
//        if (insertedNotification != null) {
//            notificationRepository.delete(insertedNotification);
//            insertedNotification = null;
//        }
//    }
//
//    @Test
//    @Transactional
//    void createNotification() throws Exception {
//        long databaseSizeBeforeCreate = getRepositoryCount();
//        // Create the Notification
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//        var returnedNotificationDTO = om.readValue(
//            restNotificationMockMvc
//                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(),
//            NotificationDTO.class
//        );
//
//        // Validate the Notification in the database
//        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
//        var returnedNotification = notificationMapper.toEntity(returnedNotificationDTO);
//        assertNotificationUpdatableFieldsEquals(returnedNotification, getPersistedNotification(returnedNotification));
//
//        insertedNotification = returnedNotification;
//    }
//
//    @Test
//    @Transactional
//    void createNotificationWithExistingId() throws Exception {
//        // Create the Notification with an existing ID
//        notification.setId(1L);
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//
//        long databaseSizeBeforeCreate = getRepositoryCount();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restNotificationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void getAllNotifications() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get all the notificationList
//        restNotificationMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
//            .andExpect(jsonPath("$.[*].notificationRefId").value(hasItem(DEFAULT_NOTIFICATION_REF_ID.toString())))
//            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
//            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.booleanValue())));
//    }
//
//    @Test
//    @Transactional
//    void getNotification() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get the notification
//        restNotificationMockMvc
//            .perform(get(ENTITY_API_URL_ID, notification.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
//            .andExpect(jsonPath("$.notificationRefId").value(DEFAULT_NOTIFICATION_REF_ID.toString()))
//            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
//            .andExpect(jsonPath("$.isRead").value(DEFAULT_IS_READ.booleanValue()));
//    }
//
//    @Test
//    @Transactional
//    void getNotificationsByIdFiltering() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        Long id = notification.getId();
//
//        defaultNotificationFiltering("id.equals=" + id, "id.notEquals=" + id);
//
//        defaultNotificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);
//
//        defaultNotificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByNotificationRefIdIsEqualToSomething() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get all the notificationList where notificationRefId equals to
//        defaultNotificationFiltering(
//            "notificationRefId.equals=" + DEFAULT_NOTIFICATION_REF_ID,
//            "notificationRefId.equals=" + UPDATED_NOTIFICATION_REF_ID
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByNotificationRefIdIsInShouldWork() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get all the notificationList where notificationRefId in
//        defaultNotificationFiltering(
//            "notificationRefId.in=" + DEFAULT_NOTIFICATION_REF_ID + "," + UPDATED_NOTIFICATION_REF_ID,
//            "notificationRefId.in=" + UPDATED_NOTIFICATION_REF_ID
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByNotificationRefIdIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get all the notificationList where notificationRefId is not null
//        defaultNotificationFiltering("notificationRefId.specified=true", "notificationRefId.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByIsReadIsEqualToSomething() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get all the notificationList where isRead equals to
//        defaultNotificationFiltering("isRead.equals=" + DEFAULT_IS_READ, "isRead.equals=" + UPDATED_IS_READ);
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByIsReadIsInShouldWork() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get all the notificationList where isRead in
//        defaultNotificationFiltering("isRead.in=" + DEFAULT_IS_READ + "," + UPDATED_IS_READ, "isRead.in=" + UPDATED_IS_READ);
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByIsReadIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        // Get all the notificationList where isRead is not null
//        defaultNotificationFiltering("isRead.specified=true", "isRead.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByUserProfileIsEqualToSomething() throws Exception {
//        UserProfile userProfile;
//        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
//            notificationRepository.saveAndFlush(notification);
//            userProfile = UserProfileResourceIT.createEntity(em);
//        } else {
//            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
//        }
//        em.persist(userProfile);
//        em.flush();
//        notification.setUserProfile(userProfile);
//        notificationRepository.saveAndFlush(notification);
//        Long userProfileId = userProfile.getId();
//        // Get all the notificationList where userProfile equals to userProfileId
//        defaultNotificationShouldBeFound("userProfileId.equals=" + userProfileId);
//
//        // Get all the notificationList where userProfile equals to (userProfileId + 1)
//        defaultNotificationShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
//    }
//
//    @Test
//    @Transactional
//    void getAllNotificationsByTypeIsEqualToSomething() throws Exception {
//        CodeTables type;
//        if (TestUtil.findAll(em, CodeTables.class).isEmpty()) {
//            notificationRepository.saveAndFlush(notification);
//            type = CodeTablesResourceIT.createEntity(em);
//        } else {
//            type = TestUtil.findAll(em, CodeTables.class).get(0);
//        }
//        em.persist(type);
//        em.flush();
//        notification.setType(type);
//        notificationRepository.saveAndFlush(notification);
//        Long typeId = type.getId();
//        // Get all the notificationList where type equals to typeId
//        defaultNotificationShouldBeFound("typeId.equals=" + typeId);
//
//        // Get all the notificationList where type equals to (typeId + 1)
//        defaultNotificationShouldNotBeFound("typeId.equals=" + (typeId + 1));
//    }
//
//    private void defaultNotificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
//        defaultNotificationShouldBeFound(shouldBeFound);
//        defaultNotificationShouldNotBeFound(shouldNotBeFound);
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned.
//     */
//    private void defaultNotificationShouldBeFound(String filter) throws Exception {
//        restNotificationMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
//            .andExpect(jsonPath("$.[*].notificationRefId").value(hasItem(DEFAULT_NOTIFICATION_REF_ID.toString())))
//            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
//            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.booleanValue())));
//
//        // Check, that the count call also returns 1
//        restNotificationMockMvc
//            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned.
//     */
//    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
//        restNotificationMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restNotificationMockMvc
//            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingNotification() throws Exception {
//        // Get the notification
//        restNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putExistingNotification() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//
//        // Update the notification
//        Notification updatedNotification = notificationRepository.findById(notification.getId()).orElseThrow();
//        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
//        em.detach(updatedNotification);
//        updatedNotification.notificationRefId(UPDATED_NOTIFICATION_REF_ID).content(UPDATED_CONTENT).isRead(UPDATED_IS_READ);
//        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);
//
//        restNotificationMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, notificationDTO.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(om.writeValueAsBytes(notificationDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//        assertPersistedNotificationToMatchAllProperties(updatedNotification);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingNotification() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        notification.setId(longCount.incrementAndGet());
//
//        // Create the Notification
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restNotificationMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, notificationDTO.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(om.writeValueAsBytes(notificationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchNotification() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        notification.setId(longCount.incrementAndGet());
//
//        // Create the Notification
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restNotificationMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(om.writeValueAsBytes(notificationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamNotification() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        notification.setId(longCount.incrementAndGet());
//
//        // Create the Notification
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restNotificationMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateNotificationWithPatch() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//
//        // Update the notification using partial update
//        Notification partialUpdatedNotification = new Notification();
//        partialUpdatedNotification.setId(notification.getId());
//
//        partialUpdatedNotification.notificationRefId(UPDATED_NOTIFICATION_REF_ID);
//
//        restNotificationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(partialUpdatedNotification))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Notification in the database
//
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//        assertNotificationUpdatableFieldsEquals(
//            createUpdateProxyForBean(partialUpdatedNotification, notification),
//            getPersistedNotification(notification)
//        );
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateNotificationWithPatch() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//
//        // Update the notification using partial update
//        Notification partialUpdatedNotification = new Notification();
//        partialUpdatedNotification.setId(notification.getId());
//
//        partialUpdatedNotification.notificationRefId(UPDATED_NOTIFICATION_REF_ID).content(UPDATED_CONTENT).isRead(UPDATED_IS_READ);
//
//        restNotificationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(partialUpdatedNotification))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Notification in the database
//
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//        assertNotificationUpdatableFieldsEquals(partialUpdatedNotification, getPersistedNotification(partialUpdatedNotification));
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingNotification() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        notification.setId(longCount.incrementAndGet());
//
//        // Create the Notification
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restNotificationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, notificationDTO.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(notificationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchNotification() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        notification.setId(longCount.incrementAndGet());
//
//        // Create the Notification
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restNotificationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(om.writeValueAsBytes(notificationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamNotification() throws Exception {
//        long databaseSizeBeforeUpdate = getRepositoryCount();
//        notification.setId(longCount.incrementAndGet());
//
//        // Create the Notification
//        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restNotificationMockMvc
//            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationDTO)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Notification in the database
//        assertSameRepositoryCount(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteNotification() throws Exception {
//        // Initialize the database
//        insertedNotification = notificationRepository.saveAndFlush(notification);
//
//        long databaseSizeBeforeDelete = getRepositoryCount();
//
//        // Delete the notification
//        restNotificationMockMvc
//            .perform(delete(ENTITY_API_URL_ID, notification.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
//    }
//
//    protected long getRepositoryCount() {
//        return notificationRepository.count();
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
//    protected Notification getPersistedNotification(Notification notification) {
//        return notificationRepository.findById(notification.getId()).orElseThrow();
//    }
//
//    protected void assertPersistedNotificationToMatchAllProperties(Notification expectedNotification) {
//        assertNotificationAllPropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
//    }
//
//    protected void assertPersistedNotificationToMatchUpdatableProperties(Notification expectedNotification) {
//        assertNotificationAllUpdatablePropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
//    }
//}
