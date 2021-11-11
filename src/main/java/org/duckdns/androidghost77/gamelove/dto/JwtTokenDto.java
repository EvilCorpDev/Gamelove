package org.duckdns.androidghost77.gamelove.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtTokenDto {

    private String tokenType = "Bearer";
    private final String token;
}
