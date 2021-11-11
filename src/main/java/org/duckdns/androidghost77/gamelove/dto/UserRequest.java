package org.duckdns.androidghost77.gamelove.dto;

import lombok.Data;
import org.duckdns.androidghost77.gamelove.enums.UserRoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserRequest {
    @NotEmpty
    private String username;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    private String roleType = UserRoleType.USER.toString();
}
