package org.duckdns.androidghost77.gamelove.exception.handler.dto;

import java.util.function.Function;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionMappingDetails {

    private String shortMessage;
    private HttpStatus httpStatus;
    private Function<Exception, String> messageFun;
}
