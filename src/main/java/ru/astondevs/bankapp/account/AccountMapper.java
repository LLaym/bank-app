package ru.astondevs.bankapp.account;

import org.springframework.stereotype.Service;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.account.model.Account;

@Service
public class AccountMapper {

    public AccountDto toAccountDto(Account createdAccount) {
        AccountDto accountDto = new AccountDto();
        accountDto.setOwner(createdAccount.getOwner());
        accountDto.setBalance(createdAccount.getBalance());
        return accountDto;
    }
}
