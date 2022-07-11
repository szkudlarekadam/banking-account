package banking.application.account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class TransferInput {

    private final String pesel;
    private final long accountFromId;
    private final long accountToId;
    private final String amount;

}
