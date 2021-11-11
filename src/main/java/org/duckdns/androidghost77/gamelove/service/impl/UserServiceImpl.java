package org.duckdns.androidghost77.gamelove.service.impl;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;
import org.duckdns.androidghost77.gamelove.enums.UserRoleType;
import org.duckdns.androidghost77.gamelove.mapper.UserMapper;
import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.UserRoleRepository;
import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.duckdns.androidghost77.gamelove.repository.model.UserRole;
import org.duckdns.androidghost77.gamelove.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        return saveUser(userRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public UserResponse createAdmin(UserRequest userRequest) {
        return saveUser(userRequest);
    }

    @Override
    public UserResponse findUserById(String id) {
        return null;
    }

    @Override
    public UserResponse findUserByUsername(String username) {
        return null;
    }

    private UserResponse saveUser(UserRequest userRequest) {
        User user = userMapper.userRequestToUser(userRequest);
        user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));

        user = userRepository.save(user);
        UserRole role = new UserRole();
        role.setUser(user);
        role.setUserRoleType(UserRoleType.valueOf(userRequest.getRoleType()));
        userRoleRepository.save(role);

        return userMapper.userToUserResponce(user);
    }
}
