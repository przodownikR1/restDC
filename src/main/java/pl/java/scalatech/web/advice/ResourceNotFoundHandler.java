package pl.java.scalatech.web.advice;

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

import pl.java.scalatech.exception.rest.ResourceNotFoundException;
import pl.java.scalatech.web.advice.dto.ErrorHolder;

@ControllerAdvice
@Slf4j
public class ResourceNotFoundHandler extends ErrorHandler<ResourceNotFoundException> {

    @Autowired
    public ResourceNotFoundHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    @ExceptionHandler({ ResourceNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorHolder> concreteExceptionHandler(ResourceNotFoundException ex, WebRequest webRequest) {
        log.error("++ 				" + "ResourceNotFoundHandler  {}  ", ex);
        ErrorHolder eHolder = null;
        if (ex.getId() != null) {
            eHolder = ErrorHolder.builder().errorMessage(messageSource.getMessage("entity.not.found.error", new Object[] { ex.getId() }, ex.getLocale()))
                    .status(HttpStatus.NOT_FOUND).build();
        } else if (ex.getName() != null) {
            eHolder = ErrorHolder.builder().errorMessage(messageSource.getMessage("entity.not.found.error", new Object[] { ex.getName() }, ex.getLocale()))
                    .status(HttpStatus.NOT_FOUND).build();
        }
        setErrorCode(eHolder, ex);
        setUrl(eHolder, webRequest);
        if (eHolder == null) {
            //TODO
            eHolder = ErrorHolder.builder().errorMessage(messageSource.getMessage("entity.not.found.error", new Object[] { ex.getName() }, ex.getLocale()))
                    .status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(eHolder, createJsonHeader(), eHolder.getStatus());

    }

}
