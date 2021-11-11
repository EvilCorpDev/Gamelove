package org.duckdns.androidghost77.gamelove.service;

import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    UserResponse createAdmin(UserRequest userRequest);

    UserResponse findUserById(String id);

    UserResponse findUserByUsername(String username);

}
