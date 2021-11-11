package org.duckdns.androidghost77.gamelove.rest;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.JwtTokenDto;
import org.duckdns.androidghost77.gamelove.security.JwtTokenProvider;
import org.duckdns.androidghost77.gamelove.security.dto.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping
    public JwtTokenDto authenticateUser(@RequestBody UserPrincipal userPrincipal) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userPrincipal.getUsername(), userPrincipal.getPassword()));

        return new JwtTokenDto(tokenProvider.generateToken(authenticate));
    }
}
