package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.UserSkill;
import com.teamsixnus.scaleup.repository.UserSkillRepository;
import com.teamsixnus.scaleup.service.dto.UserSkillDTO;
import com.teamsixnus.scaleup.service.mapper.UserSkillMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teamsixnus.scaleup.domain.UserSkill}.
 */
@Service
@Transactional
public class UserSkillService {

    private static final Logger log = LoggerFactory.getLogger(UserSkillService.class);

    private final UserSkillRepository userSkillRepository;

    private final UserSkillMapper userSkillMapper;

    public UserSkillService(UserSkillRepository userSkillRepository, UserSkillMapper userSkillMapper) {
        this.userSkillRepository = userSkillRepository;
        this.userSkillMapper = userSkillMapper;
    }

    /**
     * Save a userSkill.
     *
     * @param userSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public UserSkillDTO save(UserSkillDTO userSkillDTO) {
        log.debug("Request to save UserSkill : {}", userSkillDTO);
        UserSkill userSkill = userSkillMapper.toEntity(userSkillDTO);
        userSkill = userSkillRepository.save(userSkill);
        return userSkillMapper.toDto(userSkill);
    }

    /**
     * Update a userSkill.
     *
     * @param userSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public UserSkillDTO update(UserSkillDTO userSkillDTO) {
        log.debug("Request to update UserSkill : {}", userSkillDTO);
        UserSkill userSkill = userSkillMapper.toEntity(userSkillDTO);
        userSkill = userSkillRepository.save(userSkill);
        return userSkillMapper.toDto(userSkill);
    }

    /**
     * Partially update a userSkill.
     *
     * @param userSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserSkillDTO> partialUpdate(UserSkillDTO userSkillDTO) {
        log.debug("Request to partially update UserSkill : {}", userSkillDTO);

        return userSkillRepository
            .findById(userSkillDTO.getId())
            .map(existingUserSkill -> {
                userSkillMapper.partialUpdate(existingUserSkill, userSkillDTO);

                return existingUserSkill;
            })
            .map(userSkillRepository::save)
            .map(userSkillMapper::toDto);
    }

    /**
     * Get all the userSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserSkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSkills");
        return userSkillRepository.findAll(pageable).map(userSkillMapper::toDto);
    }

    /**
     * Get one userSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserSkillDTO> findOne(Long id) {
        log.debug("Request to get UserSkill : {}", id);
        return userSkillRepository.findById(id).map(userSkillMapper::toDto);
    }

    /**
     * Delete the userSkill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserSkill : {}", id);
        userSkillRepository.deleteById(id);
    }
}
