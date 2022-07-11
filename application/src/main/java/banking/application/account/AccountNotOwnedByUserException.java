package banking.application.account;

public class AccountNotOwnedByUserException extends RuntimeException {

    public AccountNotOwnedByUserException(String message) {
        super(message);
    }
}
