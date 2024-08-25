package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.Activity;
import com.teamsixnus.scaleup.domain.Skill;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.service.dto.ActivityDTO;
import com.teamsixnus.scaleup.service.dto.SkillDTO;
import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activity} and its DTO {@link ActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {
    @Mapping(target = "creatorProfile", source = "creatorProfile", qualifiedByName = "userProfileId")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillId")
    ActivityDTO toDto(Activity s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("skillId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SkillDTO toDtoSkillId(Skill skill);
}
