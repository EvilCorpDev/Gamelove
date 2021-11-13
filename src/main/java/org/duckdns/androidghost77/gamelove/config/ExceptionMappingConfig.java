package org.duckdns.androidghost77.gamelove.config;

import io.jsonwebtoken.MalformedJwtException;
import org.duckdns.androidghost77.gamelove.exception.GameNotFoundException;
import org.duckdns.androidghost77.gamelove.exception.handler.ExceptionHandlingMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Configuration
public class ExceptionMappingConfig {

    @Bean
    public ExceptionHandlingMapping exceptionHandlingMapping() {
        return ExceptionHandlingMapping.builder()
                .add(GameNotFoundException.class, Exception::getMessage, "Not Found", HttpStatus.UNAUTHORIZED)
                .add(MethodArgumentNotValidException.class, this::getFieldError, "Filed invalid", HttpStatus.BAD_REQUEST)
                .add(BadCredentialsException.class, Exception::getMessage, "Invalid credentials", HttpStatus.UNAUTHORIZED)
                .add(MalformedJwtException.class, exc -> "You're not authorized, please login", "Unauthorized", HttpStatus.UNAUTHORIZED)
                .add(AccessDeniedException.class, Exception::getMessage, "You are not allowed to access this resource", HttpStatus.FORBIDDEN)
                .build();
    }

    private String getFieldError(Exception exc) {
        if (exc instanceof MethodArgumentNotValidException) {
            String field = ((MethodArgumentNotValidException) exc).getFieldError().getField();
            String msg = ((MethodArgumentNotValidException) exc).getFieldError().getDefaultMessage();

            return String.format("Field %s is invalid: %s", field, msg);
        }
        return exc.getMessage();
    }
}
