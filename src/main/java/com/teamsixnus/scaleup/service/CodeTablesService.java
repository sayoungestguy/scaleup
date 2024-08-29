package com.teamsixnus.scaleup.service;

import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.repository.CodeTablesRepository;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import com.teamsixnus.scaleup.service.mapper.CodeTablesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teamsixnus.scaleup.domain.CodeTables}.
 */
@Service
@Transactional
public class CodeTablesService {

    private static final Logger log = LoggerFactory.getLogger(CodeTablesService.class);

    private final CodeTablesRepository codeTablesRepository;

    private final CodeTablesMapper codeTablesMapper;

    public CodeTablesService(CodeTablesRepository codeTablesRepository, CodeTablesMapper codeTablesMapper) {
        this.codeTablesRepository = codeTablesRepository;
        this.codeTablesMapper = codeTablesMapper;
    }

    /**
     * Save a codeTables.
     *
     * @param codeTablesDTO the entity to save.
     * @return the persisted entity.
     */
    public CodeTablesDTO save(CodeTablesDTO codeTablesDTO) {
        log.debug("Request to save CodeTables : {}", codeTablesDTO);
        CodeTables codeTables = codeTablesMapper.toEntity(codeTablesDTO);
        codeTables = codeTablesRepository.save(codeTables);
        return codeTablesMapper.toDto(codeTables);
    }

    /**
     * Update a codeTables.
     *
     * @param codeTablesDTO the entity to save.
     * @return the persisted entity.
     */
    public CodeTablesDTO update(CodeTablesDTO codeTablesDTO) {
        log.debug("Request to update CodeTables : {}", codeTablesDTO);
        CodeTables codeTables = codeTablesMapper.toEntity(codeTablesDTO);
        codeTables.setIsPersisted();
        codeTables = codeTablesRepository.save(codeTables);
        return codeTablesMapper.toDto(codeTables);
    }

    /**
     * Partially update a codeTables.
     *
     * @param codeTablesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CodeTablesDTO> partialUpdate(CodeTablesDTO codeTablesDTO) {
        log.debug("Request to partially update CodeTables : {}", codeTablesDTO);

        return codeTablesRepository
            .findById(codeTablesDTO.getId())
            .map(existingCodeTables -> {
                codeTablesMapper.partialUpdate(existingCodeTables, codeTablesDTO);

                return existingCodeTables;
            })
            .map(codeTablesRepository::save)
            .map(codeTablesMapper::toDto);
    }

    /**
     * Get all the codeTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CodeTablesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CodeTables");
        return codeTablesRepository.findAll(pageable).map(codeTablesMapper::toDto);
    }

    /**
     * Get one codeTables by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CodeTablesDTO> findOne(Long id) {
        log.debug("Request to get CodeTables : {}", id);
        return codeTablesRepository.findById(id).map(codeTablesMapper::toDto);
    }

    /**
     * Delete the codeTables by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CodeTables : {}", id);
        codeTablesRepository.deleteById(id);
    }
}
