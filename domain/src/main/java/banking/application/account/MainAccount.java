package banking.application.account;

import static javax.persistence.GenerationType.SEQUENCE;

import banking.application.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter()
@Entity
@Table(name = "accounts")
public class MainAccount {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private BigDecimal balance;

    private Currency currency;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.EAGER)
    @JsonBackReference
    private final List<Account> subaccounts = new ArrayList<>();

    @OneToOne(mappedBy = "account")
    private User user;

    public void addSubaccount(String currency) {
        Account subaccount = new Account();
        subaccount.init(this, currency);
        subaccounts.add(subaccount);
    }

    public void makeDeposit(BigDecimal amount) {
        this.balance = balance.add(amount).setScale(2, RoundingMode.DOWN);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount).setScale(2, RoundingMode.DOWN);
    }

    public void init(BigDecimal balance) {
        this.balance = balance;
        this.currency = Currency.PLN;
    }

}
