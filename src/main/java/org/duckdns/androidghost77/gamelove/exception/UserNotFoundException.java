package org.duckdns.androidghost77.gamelove.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final String userId;

    public UserNotFoundException(String message, String userId) {
        super(message);
        this.userId = userId;
    }
}
