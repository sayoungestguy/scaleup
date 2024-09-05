package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.domain.Skill;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.domain.UserSkill;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import com.teamsixnus.scaleup.service.dto.SkillDTO;
import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
import com.teamsixnus.scaleup.service.dto.UserSkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSkill} and its DTO {@link UserSkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSkillMapper extends EntityMapper<UserSkillDTO, UserSkill> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillId")
    @Mapping(target = "codeTables", source = "codeTables", qualifiedByName = "codeTablesId")
    UserSkillDTO toDto(UserSkill s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("skillId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SkillDTO toDtoSkillId(Skill skill);

    @Named("codeTablesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CodeTablesDTO toDtoCodeTablesId(CodeTables codeTables);
}
