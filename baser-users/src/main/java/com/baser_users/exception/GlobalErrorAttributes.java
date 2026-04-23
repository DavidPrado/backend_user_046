package com.baser_users.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        Map<String, Object> map = new HashMap<>();

        if (error instanceof WebExchangeBindException ex) {
            map.put("status", HttpStatus.BAD_REQUEST.value());
            map.put("error", "Bad Request");
            map.put("message", "Validation failure");

            Map<String, String> fieldErrors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                    fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
            );
            map.put("errors", fieldErrors);

        } else if (error instanceof ResponseStatusException ex) {
            map.put("status", ex.getStatusCode().value());
            map.put("message", ex.getReason());
            map.put("error", ((HttpStatus)ex.getStatusCode()).getReasonPhrase());
        } else {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", "Ocorreu um erro interno no servidor");
            map.put("error", "Internal Server Error");
        }

        map.put("timestamp", LocalDateTime.now().toString());
        map.put("path", request.path());
        return map;
    }
}