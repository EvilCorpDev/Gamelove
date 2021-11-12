package org.duckdns.androidghost77.gamelove.mapper;

import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;
import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.duckdns.androidghost77.gamelove.security.dto.UserPrincipal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userPrincipalToUser(UserPrincipal userRequest);

    UserResponse userPrincipalToUserResponse(UserPrincipal user);

    UserPrincipal userRequestToUserPrincipal(UserRequest userRequest);
}
