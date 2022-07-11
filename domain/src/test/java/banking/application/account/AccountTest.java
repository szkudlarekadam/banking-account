package banking.application.account;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void shouldExchangeToPln() {
        // given
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("5000"));
        account.setCurrency(Currency.USD);

        // when
        account.exchangeToPln(Currency.PLN, BigDecimal.valueOf(2));

        // then
        assertEquals(new BigDecimal("10000.00"), account.getBalance());
    }

    @Test
    void shouldExchangeFromPln() {
        // given
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("5000"));
        account.setCurrency(Currency.PLN);

        // when
        account.exchangeFromPln("USD", BigDecimal.valueOf(5));

        // then
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    void shouldAddAmountToAccount() {
        // given
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("5000"));
        account.setCurrency(Currency.PLN);

        // when
        account.makeDeposit(BigDecimal.valueOf(3500));

        // then
        assertEquals(new BigDecimal("8500.00"), account.getBalance());
    }

    @Test
    void shouldSubtractBalance() {
        // given
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(2000));

        // when
        account.withdraw(BigDecimal.valueOf(1000));

        // then
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    void shouldBeMainAccount() {
        // given
        Account account = new Account();
        account.setId(1L);

        MainAccount mainAccount = new MainAccount();
        mainAccount.setId(1L);
        mainAccount.getSubaccounts().add(account);
        account.setAccount(mainAccount);

        // when
        boolean result = account.isMainAccount();

        // then
        assertTrue(result);
    }

    @Test
    void shouldNotBeMainAccount() {
        // given
        Account account = new Account();
        account.setId(2L);

        MainAccount mainAccount = new MainAccount();
        mainAccount.setId(1L);
        mainAccount.getSubaccounts().add(account);
        account.setAccount(mainAccount);

        // when
        boolean result = account.isMainAccount();

        // then
        assertFalse(result);
    }

    @Test
    void shouldCreateInit() {
        // given
        MainAccount mainAccount = new MainAccount();
        Account account = new Account();

        // when
        account.init(mainAccount, "PLN");

        // then
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertEquals(Currency.PLN, account.getCurrency());
        assertEquals(mainAccount, account.getAccount());
    }

}