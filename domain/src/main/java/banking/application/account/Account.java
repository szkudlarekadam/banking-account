package banking.application.account;

import static javax.persistence.GenerationType.SEQUENCE;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "subaccounts")
@Entity
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class Account {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private BigDecimal balance;

    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private MainAccount account;

    public void init(MainAccount mainAccount, String currency) {
        this.balance = BigDecimal.ZERO;
        this.currency = Currency.valueOf(currency);
        this.account = mainAccount;
    }

    public void exchangeToPln(Currency currency, BigDecimal exchangeRate) {
        this.currency = currency;
        this.balance = this.balance.multiply(exchangeRate).setScale(2, RoundingMode.DOWN);
    }

    public void exchangeFromPln(String currency, BigDecimal exchangeRate) {
        this.currency = Currency.valueOf(currency);
        this.balance = this.balance.divide(exchangeRate, 2, RoundingMode.DOWN);
    }

    public void makeDeposit(BigDecimal amount) {
        this.balance = balance.add(amount).setScale(2, RoundingMode.DOWN);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount).setScale(2, RoundingMode.DOWN);
    }

    public boolean isMainAccount() {
        return id.equals(account.getId());
    }

}
