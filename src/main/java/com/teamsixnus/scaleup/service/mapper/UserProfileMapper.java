package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.User;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.service.dto.UserDTO;
import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UserProfileDTO toDto(UserProfile s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
