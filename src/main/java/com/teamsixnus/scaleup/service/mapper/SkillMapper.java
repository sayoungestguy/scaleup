package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.Skill;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.service.dto.SkillDTO;
import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    SkillDTO toDto(Skill s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
