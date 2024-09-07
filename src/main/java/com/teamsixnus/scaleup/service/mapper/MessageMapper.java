package com.teamsixnus.scaleup.service.mapper;

import com.teamsixnus.scaleup.domain.Message;
import com.teamsixnus.scaleup.domain.UserProfile;
import com.teamsixnus.scaleup.service.dto.MessageDTO;
import com.teamsixnus.scaleup.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "senderProfile", source = "senderProfile", qualifiedByName = "userProfileId")
    @Mapping(target = "receiverProfile", source = "receiverProfile", qualifiedByName = "userProfileId")
    MessageDTO toDto(Message s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
