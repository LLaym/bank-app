package ru.astondevs.bankapp.transaction;

import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.transaction.dto.DepositCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionDto;
import ru.astondevs.bankapp.transaction.dto.WithdrawCreationDto;

public interface TransactionService {
    TransactionDto create(TransactionCreationDto transactionCreationDto);

    AccountDto deposit(Long accountId, DepositCreationDto depositCreationDto);

    AccountDto withdraw(Long accountId, WithdrawCreationDto withdrawCreationDto);
}
