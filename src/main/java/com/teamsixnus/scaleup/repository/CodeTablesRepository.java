package com.teamsixnus.scaleup.repository;

import com.teamsixnus.scaleup.domain.CodeTables;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CodeTables entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeTablesRepository extends JpaRepository<CodeTables, Long>, JpaSpecificationExecutor<CodeTables> {}
