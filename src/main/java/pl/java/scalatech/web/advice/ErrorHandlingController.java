package pl.java.scalatech.web.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.java.scalatech.error.ErrorStatus;
import pl.java.scalatech.exception.UserCreationException;
import pl.java.scalatech.exception.UserNotFoundException;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(UserCreationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorStatus handleUserCreationException(HttpServletRequest req, UserCreationException ex) {
        return new ErrorStatus(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorStatus handleUserNotFoundException(HttpServletRequest req, UserCreationException ex) {
        return new ErrorStatus(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorStatus handleTypeMismatchException(HttpServletRequest req, TypeMismatchException ex) {
        return new ErrorStatus(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorStatus handleAnyException(HttpServletRequest req, Exception ex) {
        return new ErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}
