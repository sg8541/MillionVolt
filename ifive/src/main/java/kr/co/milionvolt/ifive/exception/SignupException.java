package kr.co.milionvolt.ifive.exception;

public class SignupException extends RuntimeException {
    public SignupException(String message, Throwable cause) {
        super(message, cause);
    }
}
