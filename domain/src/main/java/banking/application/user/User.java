package banking.application.user;

import banking.application.account.MainAccount;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
@Entity
@Table(name = "users")
public class User {

    private String name;

    private String surname;

    @Id
    private String pesel;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private MainAccount account;

    public boolean hasAccountWithId(Long id) {
        if (id.equals(account.getId())) {
            return true;
        }

        return account.getSubaccounts().stream()
              .anyMatch(acc -> id.equals(acc.getId()));
    }
}
