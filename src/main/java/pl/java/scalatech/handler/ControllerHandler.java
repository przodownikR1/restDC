package pl.java.scalatech.handler;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;



@Component
@Aspect
@Slf4j
public class ControllerHandler {
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) && target(controller) && args(model,..)")
	public String handleException(ProceedingJoinPoint jp, Object controller, Model model) throws Throwable {
		String view = null;
		
		try {
			view = (String) jp.proceed();
		} catch (DataAccessException e) {
			log.error("error in {}", controller.getClass().getSimpleName(), e);
			model.addAttribute("errorMessage", e.getMessage());
			return "errorPage";
		}
		return view;
	}
}
