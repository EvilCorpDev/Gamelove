package org.duckdns.androidghost77.gamelove.rest;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;
import org.duckdns.androidghost77.gamelove.enums.UserRoleType;
import org.duckdns.androidghost77.gamelove.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // TODO: Fix Validation
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) {
        if (UserRoleType.ADMIN.toString().equals(userRequest.getRoleType())) {
            return userService.createAdmin(userRequest);
        }
        return userService.createUser(userRequest);
    }
}
