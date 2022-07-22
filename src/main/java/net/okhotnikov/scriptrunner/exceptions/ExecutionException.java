package net.okhotnikov.scriptrunner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ExecutionException extends RuntimeException {
    public ExecutionException(String message) {
        super(message);
    }
}
