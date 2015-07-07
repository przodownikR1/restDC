package pl.java.scalatech.exception.rest;

import lombok.ToString;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ToString
public class BadRequestException extends RestException {
    private static final long serialVersionUID = 1L;

}
