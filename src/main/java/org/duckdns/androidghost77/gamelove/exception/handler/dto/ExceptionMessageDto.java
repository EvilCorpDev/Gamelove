package org.duckdns.androidghost77.gamelove.exception.handler.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
public class ExceptionMessageDto {

    private final String shortMessage;
    private final String message;
    private final ZonedDateTime timestamp = ZonedDateTime.now();
}
