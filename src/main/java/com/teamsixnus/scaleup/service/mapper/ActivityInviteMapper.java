package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.Activity;
import com.teamsixnus.scaleup.domain.ActivityInvite;
import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.service.dto.ActivityDTO;
import com.teamsixnus.scaleup.service.dto.ActivityInviteDTO;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActivityInvite} and its DTO {@link ActivityInviteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActivityInviteMapper extends EntityMapper<ActivityInviteDTO, ActivityInvite> {
    @Mapping(target = "activity", source = "activity", qualifiedByName = "activityId")
    @Mapping(target = "inviteeProfile", source = "inviteeProfile", qualifiedByName = "userProfileId")
    @Mapping(target = "status", source = "status", qualifiedByName = "codeTablesId")
    ActivityInviteDTO toDto(ActivityInvite s);

    @Named("activityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActivityDTO toDtoActivityId(Activity activity);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("codeTablesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CodeTablesDTO toDtoCodeTablesId(CodeTables codeTables);
}
