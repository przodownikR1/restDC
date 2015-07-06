package pl.java.scalatech.exception;

public class UserNotFoundException extends RuntimeException {

    private String field;

    private static final long serialVersionUID = -5093203795047101602L;

    public UserNotFoundException() {
        super();

    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String field) {
        this.field = field;
    }

}
