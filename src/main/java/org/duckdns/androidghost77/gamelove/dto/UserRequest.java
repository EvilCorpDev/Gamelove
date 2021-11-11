package org.duckdns.androidghost77.gamelove.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserRequest {
    @NotEmpty
    private String username;
    @Email
    private String email;
    @NotEmpty
    private char[] password;
}
