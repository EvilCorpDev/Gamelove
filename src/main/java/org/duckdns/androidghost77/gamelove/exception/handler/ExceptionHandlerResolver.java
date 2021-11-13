package org.duckdns.androidghost77.gamelove.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.androidghost77.gamelove.exception.handler.dto.ExceptionMappingDetails;
import org.duckdns.androidghost77.gamelove.exception.handler.dto.ExceptionMessageDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerResolver {

    private final ExceptionHandlingMapping exceptionHandlingMapping;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> exception(Exception exc, WebRequest request) {
        log.error("Handling exception", exc);
        ExceptionMappingDetails mappingDetails = exceptionHandlingMapping.getOrDefault(exc.getClass());
        ExceptionMessageDto messageDto = new ExceptionMessageDto(mappingDetails.getShortMessage(),
                mappingDetails.getMessageFun().apply(exc));

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(mappingDetails.getHttpStatus())) {
            request.setAttribute("javax.servlet.error.exception", exc, 0);
        }

        return new ResponseEntity<>(messageDto, new HttpHeaders(), mappingDetails.getHttpStatus());
    }
}
