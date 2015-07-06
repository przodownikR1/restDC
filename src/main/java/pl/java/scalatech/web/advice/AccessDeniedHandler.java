package pl.java.scalatech.web.advice;

import java.nio.file.AccessDeniedException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import pl.java.scalatech.web.advice.dto.ErrorHolder;

@ControllerAdvice
@Slf4j
public class AccessDeniedHandler extends ErrorHandler<AccessDeniedException> {

    @Autowired
    public AccessDeniedHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<ErrorHolder> concreteExceptionHandler(AccessDeniedException ex, WebRequest webRequest) {
        log.error("++ 			 " + "AccessDeniedHandler  {}  ", ex);
        ErrorHolder eHolder = ErrorHolder.builder().errorMessage(ex.getMessage()).status(HttpStatus.UNAUTHORIZED).build();
        setErrorCode(eHolder, ex);
        setUrl(eHolder, webRequest);
        return new ResponseEntity<>(eHolder, createJsonHeader(), HttpStatus.UNAUTHORIZED);
    }

}
