package banking.application.account;

import banking.application.account.dto.ConvertAmountInputDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class ConvertAmountInputDtoToTransferInputConverter implements Converter<ConvertAmountInputDto, TransferInput> {

    @Override
    public TransferInput convert(ConvertAmountInputDto source) {
        return TransferInput.builder()
              .accountFromId(source.getAccountFromId())
              .accountToId(source.getAccountToId())
              .pesel(source.getPesel())
              .amount(source.getAmount())
              .build();
    }
}
