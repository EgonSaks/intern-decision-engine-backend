package ee.taltech.inbankbackend.exceptions;

public class NotEligibleAgeException extends Throwable {
    private final String message;
    private final Throwable cause;

    public NotEligibleAgeException(String message) {
        this(message, null);
    }

    public NotEligibleAgeException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
