package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.Skill;
import com.teamsixnus.scaleup.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {}
