package banking.application.account;

import banking.application.nbp.NbpConnector;
import banking.application.user.User;
import banking.application.user.UserNotFoundException;
import banking.application.user.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
class AccountFacade {
    private final MainAccountRepository mainAccountRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final MainAccountToAccountConverter mainAccountToAccountConverter;
    private final NbpConnector nbpConnector;

    public AccountFacade(MainAccountRepository mainAccountRepository, UserRepository userRepository,
          AccountRepository accountRepository, MainAccountToAccountConverter mainAccountToAccountConverter, NbpConnector nbpConnector) {
        this.mainAccountRepository = mainAccountRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.mainAccountToAccountConverter = mainAccountToAccountConverter;
        this.nbpConnector = nbpConnector;
    }

    public MainAccount addAccount(String pesel, String accountId, String currency) {
        Optional<User> user = userRepository.findByPesel(pesel);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with pesel: " + pesel);
        }

        MainAccount account = user.get().getAccount();


        if (!account.getId().equals(Long.valueOf(accountId))) {
            throw new AccountNotOwnedByUserException("Account is not owned by provided user");
        }

        account.addSubaccount(currency);
        return mainAccountRepository.save(account);
    }

    public Account getInformationAboutAccount(String accountId, String currency) {
        Account account = getAccount(accountId);

        if (Currency.findByName(currency) == null) {
            throw new CurrencyIsNotSupportedException("Currency " + currency + " is not supported");
        }

        return exchangeAccountBalanceCurrencies(currency, account);
    }

    private Account getAccount(String accountId) {
        Optional<MainAccount> mainAccount = mainAccountRepository.findById(Long.valueOf(accountId));
        Account account;

        if (mainAccount.isPresent()) {
            account =  mainAccountToAccountConverter.convert(mainAccount.get());
        }
        else {
            Optional<Account> subaccount = accountRepository.findById(Long.valueOf(accountId));

            if (subaccount.isEmpty()) {
                throw new AccountNotFoundException("Account with id: " + accountId + " not found");
            }

            account = subaccount.get();
        }
        return account;
    }

    private Account exchangeAccountBalanceCurrencies(String currency, Account account) {
        if (currency.equals(account.getCurrency().getValue())) {
            return account;
        }

        if (currency.equals(Currency.PLN.getValue())) {
            account.exchangeToPln(Currency.PLN, nbpConnector.getExchangeRate(account.getCurrency().getValue()));
            return account;
        }

        account.exchangeFromPln(currency, nbpConnector.getExchangeRate(currency));
        return account;
    }

    public void deposit(String accountId, String currency, BigDecimal amount) {
        Optional<MainAccount> mainAccountOpt = mainAccountRepository.findById(Long.valueOf(accountId));

        if (mainAccountOpt.isPresent()) {
            MainAccount mainAccount = mainAccountOpt.get();

            mainAccount.makeDeposit(calculateAmount(amount, mainAccount.getCurrency(), currency));
            mainAccountRepository.save(mainAccount);
            return;
        }

        Optional<Account> subaccountOpt = accountRepository.findById(Long.valueOf(accountId));

        if (subaccountOpt.isEmpty()) {
            throw new AccountNotFoundException("Account with id: " + accountId + " not found");
        }

        Account account = subaccountOpt.get();
        account.makeDeposit(calculateAmount(amount, account.getCurrency(), currency));
        accountRepository.save(account);

    }

    private BigDecimal calculateAmount(BigDecimal amount, Currency accountCurrency, String currency) {
        if (currency.equals(accountCurrency.getValue())) {
            return amount;
        }

        if (currency.equals(Currency.PLN.getValue())) {
            BigDecimal rate = nbpConnector.getExchangeRate(accountCurrency.getValue());
            return amount.divide(rate, 2, RoundingMode.DOWN);
        }

        BigDecimal rate = nbpConnector.getExchangeRate(currency);
        return amount.multiply(rate).setScale(2, RoundingMode.DOWN);
    }

    // TODO: Unfortunately I have chosen bad model and due to this fact logic is too complicated
    @Transactional(rollbackOn = Exception.class)
    public Account transfer(TransferInput transferInput) {
        String pesel = transferInput.getPesel();
        Optional<User> userOpt = userRepository.findByPesel(pesel);
        BigDecimal amount = new BigDecimal(transferInput.getAmount());

        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with pesel: " + pesel);
        }

        User user = userOpt.get();
        boolean hasUserAccount = user.hasAccountWithId(transferInput.getAccountFromId());

        if (!hasUserAccount) {
            throw new SecurityException("Account doesnt belong to user");
        }

        Account accountFrom = getAccount(String.valueOf(transferInput.getAccountFromId()));
        Account accountTo = getAccount(String.valueOf(transferInput.getAccountToId()));

        if (accountFrom.getBalance().compareTo(new BigDecimal(transferInput.getAmount())) < 0) {
            throw new NotEnoughMoneyException("User doesnt have enough money to transfer");
        }

        if (accountTo.getCurrency().equals(accountFrom.getCurrency())) {
            transferWithTheSameCurrency(amount, accountFrom, accountTo);
            return getAccount(String.valueOf(accountFrom.getId()));
        }

        if (Currency.PLN.equals(accountFrom.getCurrency())) {
            transferFromPln(amount, accountFrom, accountTo);
            return getAccount(String.valueOf(accountFrom.getId()));
        }

        tranfserToPln(amount, accountFrom, accountTo);
        return getAccount(String.valueOf(accountFrom.getId()));
    }

    private void tranfserToPln(BigDecimal amount, Account accountFrom, Account accountTo) {
        boolean isAccountFromMain = accountFrom.isMainAccount();
        boolean isAccountToMain = accountTo.isMainAccount();
        BigDecimal exchangeRate = nbpConnector.getExchangeRate(accountFrom.getCurrency().getValue());

        if (isAccountFromMain) {
            MainAccount mainAccountFrom = mainAccountRepository.findById(accountFrom.getId()).get();
            mainAccountFrom.withdraw(amount);
            mainAccountRepository.save(mainAccountFrom);
        }
        else {
            accountFrom.withdraw(amount);
            accountRepository.save(accountFrom);
        }

        if (isAccountToMain) {
            MainAccount mainAccountTo = mainAccountRepository.findById(accountTo.getId()).get();
            mainAccountTo.makeDeposit(amount.multiply(exchangeRate).setScale(2, RoundingMode.DOWN));
        } else {
            accountTo.withdraw(amount.multiply(exchangeRate).setScale(2, RoundingMode.DOWN));
            accountRepository.save(accountTo);
        }
    }

    private void transferFromPln(BigDecimal amount, Account accountFrom, Account accountTo) {
        boolean isAccountFromMain = accountFrom.isMainAccount();
        boolean isAccountToMain = accountTo.isMainAccount();
        BigDecimal exchangeRate = nbpConnector.getExchangeRate(accountTo.getCurrency().getValue());

        if (isAccountFromMain) {
            MainAccount mainAccountFrom = mainAccountRepository.findById(accountFrom.getId()).get();
            mainAccountFrom.withdraw(amount);
            mainAccountRepository.save(mainAccountFrom);
        }
        else {
            accountFrom.withdraw(amount);
            accountRepository.save(accountFrom);
        }

        if (isAccountToMain) {
            MainAccount mainAccountTo = mainAccountRepository.findById(accountFrom.getId()).get();
            mainAccountTo.makeDeposit(amount.divide(exchangeRate,2 , RoundingMode.DOWN));
            mainAccountRepository.save(mainAccountTo);
        } else {
            accountTo.makeDeposit(amount.divide(exchangeRate,2, RoundingMode.DOWN));
            accountRepository.save(accountTo);
        }
    }

    private void transferWithTheSameCurrency(BigDecimal amount, Account accountFrom, Account accountTo) {
        boolean isAccountFromMain = accountFrom.isMainAccount();
        boolean isAccountToMain = accountTo.isMainAccount();

        if (isAccountFromMain) {
            MainAccount mainAccountFrom = mainAccountRepository.findById(accountFrom.getId()).get();
            mainAccountFrom.withdraw(amount);
            mainAccountRepository.save(mainAccountFrom);
        }
        else {
            accountFrom.withdraw(amount);
            accountRepository.save(accountFrom);
        }

        if (isAccountToMain) {
            MainAccount mainAccountTo = mainAccountRepository.findById(accountFrom.getId()).get();
            mainAccountTo.makeDeposit(amount);
            mainAccountRepository.save(mainAccountTo);
        }
        else {
            accountTo.makeDeposit(amount);
            accountRepository.save(accountTo);
        }
    }
}
