package org.duckdns.androidghost77.gamelove.service.impl;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;
import org.duckdns.androidghost77.gamelove.mapper.UserMapper;
import org.duckdns.androidghost77.gamelove.security.DbUserDetailsManager;
import org.duckdns.androidghost77.gamelove.security.dto.UserPrincipal;
import org.duckdns.androidghost77.gamelove.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final DbUserDetailsManager userDetailsManager;
    private final UserMapper userMapper;

    @Override
    public void createUser(UserRequest userRequest) {
        saveUser(userRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void createAdmin(UserRequest userRequest) {
        saveUser(userRequest);
    }

    @Override
    public void deleteUser(String userId) {
        userDetailsManager.deleteUserById(userId);
    }

    @Override
    public UserResponse findUserById(String id) {
        return userMapper.userPrincipalToUserResponse(
                (UserPrincipal) userDetailsManager.loadUserById(id));
    }

    @Override
    public UserResponse findUserByUsername(String username) {
        return userMapper.userPrincipalToUserResponse(
                (UserPrincipal) userDetailsManager.loadUserByUsername(username));
    }

    private void saveUser(UserRequest userRequest) {
        UserPrincipal userPrincipal = userMapper.userRequestToUserPrincipal(userRequest);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(userRequest.getRoleType())));
        userDetailsManager.createUser(userPrincipal);
    }
}
