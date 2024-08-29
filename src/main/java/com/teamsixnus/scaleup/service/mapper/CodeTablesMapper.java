package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CodeTables} and its DTO {@link CodeTablesDTO}.
 */
@Mapper(componentModel = "spring")
public interface CodeTablesMapper extends EntityMapper<CodeTablesDTO, CodeTables> {}
