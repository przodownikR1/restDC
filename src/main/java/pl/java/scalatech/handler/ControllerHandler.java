package pl.java.scalatech.handler;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.google.common.base.Stopwatch;

@Component
@Aspect
@Slf4j
public class ControllerHandler {
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) && target(controller) && args(model,..)")
    public String handleException(ProceedingJoinPoint jp, Object controller, Model model) throws Throwable {
        String view = null;
        Stopwatch sw = Stopwatch.createStarted();

        try {
            view = (String) jp.proceed();
        } catch (DataAccessException e) {
            log.error("error in {}", controller.getClass().getSimpleName(), e);

            model.addAttribute("errorMessage", e.getMessage());
            return "errorPage";
        } finally {
            sw.stop();
            log.info("method : {} ->  took {} ms", jp.getSignature().getName(), sw.elapsed(TimeUnit.MICROSECONDS));
        }

        return view;
    }
}
