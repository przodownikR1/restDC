package pl.java.scalatech.exception.rest;

import java.util.Locale;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class RestException extends RuntimeException {
    /**
	 * 
	 */
    private static final long serialVersionUID = 3962976638163993460L;
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Locale locale;

    public RestException(Long id, Locale locale) {
        super();
        this.id = id;
        this.locale = locale;
    }

    public RestException(String name, Locale locale) {
        super();
        this.name = name;
        this.locale = locale;
    }

}
