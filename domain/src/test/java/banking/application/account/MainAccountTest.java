package banking.application.account;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MainAccountTest {

    @Test
    void shouldAddAmountToAccount() {
        // given
        MainAccount account = new MainAccount();
        account.setId(1L);
        account.setBalance(new BigDecimal("5000"));
        account.setCurrency(Currency.PLN);

        // when
        account.makeDeposit(BigDecimal.valueOf(3500));

        // then
        assertEquals(new BigDecimal("8500.00"), account.getBalance());
    }

    @Test
    void shouldAddNewSubaccount() {
        // given
        MainAccount account = new MainAccount();
        account.setId(1L);
        account.setBalance(new BigDecimal("5000"));
        account.setCurrency(Currency.PLN);

        // when
        account.addSubaccount("USD");

        // then
        assertEquals(1, account.getSubaccounts().size());
        assertEquals(Currency.USD, account.getSubaccounts().get(0).getCurrency());
        assertEquals(BigDecimal.ZERO, account.getSubaccounts().get(0).getBalance());
    }

    @Test
    void shouldInitAccount() {
        // given
        MainAccount account = new MainAccount();
        BigDecimal balance = BigDecimal.valueOf(2000);

        // when
        account.init(balance);

        // then
        assertEquals(balance, account.getBalance());
        assertEquals(Currency.PLN, account.getCurrency());
    }

    @Test
    void shouldSubtractBalance() {
        // given
        MainAccount account = new MainAccount();
        account.setBalance(BigDecimal.valueOf(2000));

        // when
        account.withdraw(BigDecimal.valueOf(1000));

        // then
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

}