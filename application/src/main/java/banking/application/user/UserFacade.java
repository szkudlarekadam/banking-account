package banking.application.user;

import banking.application.account.MainAccount;
import java.math.BigDecimal;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
class UserFacade {
    private final UserRepository userRepository;

    public UserFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public User save(User user) {
        validate(user);
        MainAccount account = prepareNewAccount(user.getAccount().getBalance());
        user.setAccount(account);
        return userRepository.save(user);
    }

    private void validate(User user) {
        new UserAgeValidator().test(user);
        Optional<User> userOpt = userRepository.findByPesel(user.getPesel());
        if (userOpt.isPresent()) {
            throw new UserExistsException("User with pesel: " + user.getPesel() + " exists");
        }
    }

    private MainAccount prepareNewAccount(BigDecimal balance) {
        MainAccount account = new MainAccount();
        account.init(balance);
        return account;
    }



}
