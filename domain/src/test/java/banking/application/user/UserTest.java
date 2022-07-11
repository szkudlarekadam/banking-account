package banking.application.user;

import static org.junit.jupiter.api.Assertions.*;

import banking.application.account.Account;
import banking.application.account.MainAccount;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldReturnTrueWhenUserHasMainAccount() {
        // given
        User user = new User();
        user.setPesel("12345678901");
        user.setName("Adam");
        user.setSurname("Szkudlarek");

        MainAccount mainAccount = new MainAccount();
        mainAccount.setUser(user);
        mainAccount.setBalance(new BigDecimal("1000"));
        mainAccount.setId(1L);

        user.setAccount(mainAccount);

        // when
        boolean result = user.hasAccountWithId(1L);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenUserHasSubaccount() {
        // given
        User user = new User();
        user.setPesel("12345678901");
        user.setName("Adam");
        user.setSurname("Szkudlarek");

        MainAccount mainAccount = new MainAccount();
        mainAccount.setUser(user);
        mainAccount.setId(1L);

        Account subaccount = new Account();
        subaccount.setId(2L);

        mainAccount.getSubaccounts().add(subaccount);

        user.setAccount(mainAccount);

        // when
        boolean result = user.hasAccountWithId(2L);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUserDoesntHaveAccount() {
        // given
        User user = new User();
        user.setPesel("12345678901");
        user.setName("Adam");
        user.setSurname("Szkudlarek");

        MainAccount mainAccount = new MainAccount();
        mainAccount.setUser(user);
        mainAccount.setId(1L);

        Account subaccount = new Account();
        subaccount.setId(2L);

        mainAccount.getSubaccounts().add(subaccount);

        user.setAccount(mainAccount);

        // when
        boolean result = user.hasAccountWithId(3L);

        // then
        assertFalse(result);
    }

}