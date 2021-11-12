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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final DbUserDetailsManager userDetailsManager;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createUser(UserRequest userRequest) {
        saveUser(userRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public void createAdmin(UserRequest userRequest) {
        saveUser(userRequest);
    }

    @Override
    public void deleteUser(String userId) {
        userDetailsManager.deleteUserById(userId);
    }

    public UserResponse getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userMapper.userPrincipalToUserResponse(userPrincipal);
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
