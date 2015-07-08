package pl.java.scalatech.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.error.ErrorStatus;
import pl.java.scalatech.exception.SimpleRestException;

@RestController
@RequestMapping("/exceptionTest")
@Profile("webTest")
public class ExceptionRestController {

    @RequestMapping("/test/{id}")
    public String test(@PathVariable("id") Long id) {
        if (id > 10) { throw new SimpleRestException(id);

        }
        return "success";
    }

    @RequestMapping("/simpleExceptionError")
    public String responseError() {
        return "Something goes wrong... :)";
    }

    @ExceptionHandler(SimpleRestException.class)
    public ErrorStatus handleException(HttpServletRequest request, Exception exception) {
        ErrorStatus error = ErrorStatus.builder().status(HttpStatus.BAD_REQUEST).message(exception.getLocalizedMessage())
                .url(request.getRequestURL().append("/exceptionTest/simpleExceptionError").toString()).build();
        return error;
    }

}
