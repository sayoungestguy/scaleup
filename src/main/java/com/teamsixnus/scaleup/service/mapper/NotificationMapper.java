package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.CodeTables;
import com.teamsixnus.scaleup.domain.Notification;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.service.dto.CodeTablesDTO;
import com.teamsixnus.scaleup.service.dto.NotificationDTO;
import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    @Mapping(target = "type", source = "type", qualifiedByName = "codeTablesId")
    NotificationDTO toDto(Notification s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("codeTablesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CodeTablesDTO toDtoCodeTablesId(CodeTables codeTables);
}
