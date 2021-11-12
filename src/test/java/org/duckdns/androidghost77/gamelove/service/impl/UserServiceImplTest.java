package org.duckdns.androidghost77.gamelove.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;
import org.duckdns.androidghost77.gamelove.enums.UserRoleType;
import org.duckdns.androidghost77.gamelove.mapper.UserMapper;
import org.duckdns.androidghost77.gamelove.security.DbUserDetailsManager;
import org.duckdns.androidghost77.gamelove.security.dto.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private DbUserDetailsManager userDetailsManager;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        //given
        String username = "ivanov123";
        String email = "super@gmail.com";
        String password = "secure password";

        UserRequest userRequest = createUserRequest(username, email, password);
        UserPrincipal userPrincipal = createUserPrincipal(username, email, password, UserRoleType.USER);

        //when
        when(userMapper.userRequestToUserPrincipal(userRequest)).thenReturn(userPrincipal);

        //then
        userService.createUser(userRequest);
        verify(userDetailsManager).createUser(userPrincipal);
    }

    @Test
    void createAdmin() {
        //given
        String username = "ivanov123";
        String email = "super@gmail.com";
        String password = "secure password";

        UserRequest userRequest = createUserRequest(username, email, password);
        UserPrincipal userPrincipal = createUserPrincipal(username, email, password, UserRoleType.ADMIN);

        //when
        when(userMapper.userRequestToUserPrincipal(userRequest)).thenReturn(userPrincipal);

        //then
        userService.createAdmin(userRequest);
        verify(userDetailsManager).createUser(userPrincipal);
    }

    @Test
    void deleteUser() {
        //given
        String userId = UUID.randomUUID().toString();

        //then
        userService.deleteUser(userId);
        verify(userDetailsManager).deleteUserById(userId);
    }

    @Test
    void findUserById() {
        //given
        String userId = UUID.randomUUID().toString();
        String username = "ivanov123";
        String email = "super@gmail.com";
        String password = "secure password";

        UserPrincipal userPrincipal = createUserPrincipal(username, email, password, UserRoleType.ADMIN);
        UserResponse expectedResult = new UserResponse(username, email);

        //when
        when(userDetailsManager.loadUserById(userId)).thenReturn(userPrincipal);
        when(userMapper.userPrincipalToUserResponse(userPrincipal)).thenReturn(expectedResult);

        //then
        UserResponse actualResult = userService.findUserById(userId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void findUserByUsername() {
        //given
        String username = "ivanov123";
        String email = "super@gmail.com";
        String password = "secure password";

        UserPrincipal userPrincipal = createUserPrincipal(username, email, password, UserRoleType.ADMIN);
        UserResponse expectedResult = new UserResponse(username, email);

        //when
        when(userDetailsManager.loadUserByUsername(username)).thenReturn(userPrincipal);
        when(userMapper.userPrincipalToUserResponse(userPrincipal)).thenReturn(expectedResult);

        //then
        UserResponse actualResult = userService.findUserByUsername(username);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private UserRequest createUserRequest(String username, String email, String password) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(username);
        userRequest.setEmail(email);
        userRequest.setPassword(password);

        return userRequest;
    }

    private UserPrincipal createUserPrincipal(String username, String email, String password, UserRoleType roleType) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUsername(username);
        userPrincipal.setEmail(email);
        userPrincipal.setPassword(password);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(roleType.toString())));

        return userPrincipal;
    }

}
