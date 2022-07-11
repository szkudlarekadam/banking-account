package banking.application;

import banking.application.account.AccountNotFoundException;
import banking.application.account.AccountNotOwnedByUserException;
import banking.application.account.CurrencyIsNotSupportedException;
import banking.application.account.NotEnoughMoneyException;
import banking.application.nbp.ExchangeCurrencyRateNotFound;
import banking.application.user.UserExistsException;
import banking.application.user.UserNotAdultException;
import banking.application.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ExceptionResponse> userExists(UserExistsException ex) {
        log.error("UserExistsException", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotAdultException.class)
    public ResponseEntity<ExceptionResponse> userNotAdult(UserNotAdultException ex) {
        log.error("UserNotAdultException", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> userNotFound(UserNotFoundException ex) {
        log.error("UserNotFoundException", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ExceptionResponse> accountNotFound(AccountNotFoundException ex) {
        log.error("AccountNotFoundException", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotOwnedByUserException.class)
    public ResponseEntity<ExceptionResponse> accountNotOwnedByUser(AccountNotOwnedByUserException ex) {
        log.error("AccountNotOwnedByUserException", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ExceptionResponse> notEnoughMoney(NotEnoughMoneyException ex) {
        log.error("AccountNotOwnedByUserException", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExchangeCurrencyRateNotFound.class)
    public ResponseEntity<ExceptionResponse> exchangeCurrencyRateNotFound(ExchangeCurrencyRateNotFound ex) {
        log.error("ExchangeCurrencyRateNotFound", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(CurrencyIsNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> exchangeCurrencyRateNotFound(CurrencyIsNotSupportedException ex) {
        log.error("CurrencyIsNotSupportedException", ex);
        return new ResponseEntity<>(prepareExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }


    private ExceptionResponse prepareExceptionResponse(RuntimeException ex) {
        return ExceptionResponse
              .builder()
              .message(ex.getMessage())
              .build();
    }

}
