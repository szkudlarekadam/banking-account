package banking.application.user;

public class UserNotAdultException extends RuntimeException {

    public UserNotAdultException(String message) {
        super(message);
    }
}
