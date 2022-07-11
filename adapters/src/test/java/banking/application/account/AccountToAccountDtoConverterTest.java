package banking.application.account;

import static org.junit.jupiter.api.Assertions.*;

import banking.application.account.dto.AccountDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AccountToAccountDtoConverterTest {

    private final AccountToAccountDtoConverter converter = new AccountToAccountDtoConverter();

    @Test
    void shouldConvert() {
        // given
        Account account = new Account();
        account.setId(1L);
        account.setCurrency(Currency.PLN);
        account.setBalance(BigDecimal.valueOf(1000));

        // when
        AccountDto result = converter.convert(account);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Currency.PLN.getValue(), result.getCurrency());
        assertEquals(BigDecimal.valueOf(1000), result.getBalance());

    }

}