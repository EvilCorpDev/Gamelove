package org.duckdns.androidghost77.gamelove.exception.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionMessageDto {

    private String shortMessage;
    private String message;
    private final ZonedDateTime timestamp = ZonedDateTime.now();
}
