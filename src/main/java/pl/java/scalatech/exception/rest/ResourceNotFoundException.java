package pl.java.scalatech.exception.rest;

import java.util.Locale;

import lombok.ToString;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ToString
public class ResourceNotFoundException extends RestException {

	public ResourceNotFoundException(Long id, Locale locale) {
		super(id, locale);
	}

	public ResourceNotFoundException(String name, Locale locale) {
		super(name, locale);
	}

	public ResourceNotFoundException() {
	 super();
	}

	private static final long serialVersionUID = 3962976638163993460L;

}