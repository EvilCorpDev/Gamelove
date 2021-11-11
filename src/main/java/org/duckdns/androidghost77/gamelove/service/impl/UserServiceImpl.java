package org.duckdns.androidghost77.gamelove.service.impl;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;
import org.duckdns.androidghost77.gamelove.mapper.UserMapper;
import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.duckdns.androidghost77.gamelove.service.PasswordService;
import org.duckdns.androidghost77.gamelove.service.UserService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final int SALT_BYTE_LENGTH = 12;
    private final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordService passwordService;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        random.nextBytes(salt);
        byte[] hash = passwordService.hashPassword(userRequest.getPassword(), salt);

        User user = userMapper.userRequestToUser(userRequest);
        user.setPasswordHash(hash);
        user.setSalt(salt);

        return userMapper.userToUserResponce(userRepository.save(user));
    }

    @Override
    public UserResponse findUserById(String id) {
        return null;
    }

    @Override
    public UserResponse findUserByUsername(String username) {
        return null;
    }
}
