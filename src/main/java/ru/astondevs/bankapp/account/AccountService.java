package ru.astondevs.bankapp.account;

import ru.astondevs.bankapp.account.dto.AccountCreationDto;
import ru.astondevs.bankapp.account.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto create(AccountCreationDto account);

    AccountDto getById(Long accountId);

    List<AccountDto> get(Integer from, Integer size);
}
