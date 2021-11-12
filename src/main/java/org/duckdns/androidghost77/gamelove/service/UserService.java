package org.duckdns.androidghost77.gamelove.service;

import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;

public interface UserService {

    void createUser(UserRequest userRequest);

    void createAdmin(UserRequest userRequest);

    void deleteUser(String userId);

    UserResponse findUserById(String id);

    UserResponse findUserByUsername(String username);

}
