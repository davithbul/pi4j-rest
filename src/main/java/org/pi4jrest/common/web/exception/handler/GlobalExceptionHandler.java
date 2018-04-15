package org.pi4jrest.common.web.exception.handler;

import org.pi4jrest.common.exceptions.ObjectAlreadyExistsException;
import org.pi4jrest.common.exceptions.ObjectNotFoundException;
import org.pi4jrest.common.web.exception.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse illegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage() != null ? e.getMessage() : "Illegal Argument passed!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorResponse objectNotFoundException(ObjectNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ErrorResponse objectAlreadyExistsException(ObjectAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public Throwable exception(Exception e) {
        log.error(e.getMessage(), e);
        return e;
    }
}
