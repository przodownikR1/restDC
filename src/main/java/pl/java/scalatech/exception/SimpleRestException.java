package pl.java.scalatech.exception;

public class SimpleRestException extends RuntimeException {

    private static final long serialVersionUID = 8097928491747676858L;
    private Long id;

    public SimpleRestException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public SimpleRestException(Long id) {
        super();
        this.id = id;
    }

}
