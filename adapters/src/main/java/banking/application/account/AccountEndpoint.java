package banking.application.account;

import banking.application.account.dto.AccountDto;
import banking.application.account.dto.ConvertAmountInputDto;
import banking.application.account.dto.CreateAccountInputDto;
import banking.application.account.dto.DepositInputDto;
import banking.application.account.dto.MainAccountDto;
import java.math.BigDecimal;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
class AccountEndpoint {

    private final AccountFacade accountFacade;
    private final MainAccountToAccountDtoConverter mainAccountToAccountDtoConverter;
    private final AccountToAccountDtoConverter accountToAccountDtoConverter;
    private final ConvertAmountInputDtoToTransferInputConverter convertAmountInputDtoToTransferInputConverter;

    public AccountEndpoint(AccountFacade accountFacade, MainAccountToAccountDtoConverter accountToAccountDtoConverter,
          AccountToAccountDtoConverter accountToAccountDtoConverter1,
          ConvertAmountInputDtoToTransferInputConverter convertAmountInputDtoToTransferInputConverter) {
        this.accountFacade = accountFacade;
        this.mainAccountToAccountDtoConverter = accountToAccountDtoConverter;
        this.accountToAccountDtoConverter = accountToAccountDtoConverter1;
        this.convertAmountInputDtoToTransferInputConverter = convertAmountInputDtoToTransferInputConverter;
    }

    @PostMapping("/{id}/subaccount")
    MainAccountDto addAccount(@Valid @RequestBody CreateAccountInputDto createAccountInputDto, @PathVariable("id") String accountId) {
        MainAccount mainAccount = accountFacade.addAccount(createAccountInputDto.getPesel(), accountId, createAccountInputDto.getCurrency());
        return mainAccountToAccountDtoConverter.convert(mainAccount);
    }

    @GetMapping("/{id}/{currency}")
    @ResponseBody AccountDto getInformationAboutAccount(@PathVariable("id") String accountId, @PathVariable String currency) {
        Account account = accountFacade.getInformationAboutAccount(accountId, currency);
        return accountToAccountDtoConverter.convert(account);
    }

    @PostMapping("/deposit")
    void deposit(@RequestBody DepositInputDto depositInputDto) {
        accountFacade.deposit(depositInputDto.getAccountId(), depositInputDto.getCurrency(), new BigDecimal(depositInputDto.getAmount()));
    }

    @PostMapping("/exchange")
    @ResponseBody AccountDto transfer(@Valid @RequestBody ConvertAmountInputDto convertAmountInputDto) {
        TransferInput transferInput = convertAmountInputDtoToTransferInputConverter.convert(convertAmountInputDto);
        Account account = accountFacade.transfer(transferInput);
        return accountToAccountDtoConverter.convert(account);
    }

}
