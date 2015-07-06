package pl.java.scalatech.web.advice;

import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import pl.java.scalatech.web.advice.dto.ErrorHolder;
import pl.java.scalatech.web.advice.dto.ErrorHolderFieldDetails;

@ControllerAdvice
@Slf4j
public class ParameterMethodHandler extends ErrorHandler<MethodArgumentNotValidException> {
    @Autowired
    public ParameterMethodHandler(MessageSource messageSource) {
        super(messageSource);
    }

    private ErrorHolder processFieldErrors(List<FieldError> fieldErrors, List<ObjectError> globalErrors, HttpStatus status) {
        ErrorHolderFieldDetails dto = new ErrorHolderFieldDetails();
        dto.setStatus(status);
        // TODO add inner error code message
        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            log.info("+++   			Adding error message: {} to field: {}", localizedErrorMessage, fieldError.getField());
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }
        for (ObjectError objectError : globalErrors) {
            String error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
            log.error("RestErrorHandler detect error :  {} ", error);
            dto.getErrors().add(error);
        }

        return dto;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        log.info("+++          fieldError {}  , currentLocale   {}", fieldError, currentLocale);

        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }
        return localizedErrorMessage;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Override
    public ResponseEntity<ErrorHolder> concreteExceptionHandler(MethodArgumentNotValidException ex, WebRequest webRequest) {
        log.info("+++				ParameterMethodHandler");
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        ErrorHolder eHolder = processFieldErrors(fieldErrors, globalErrors, HttpStatus.BAD_REQUEST);
        setErrorCode(eHolder, ex);
        setUrl(eHolder, webRequest);
        return new ResponseEntity<>(eHolder, createJsonHeader(), eHolder.getStatus());
    }

}
