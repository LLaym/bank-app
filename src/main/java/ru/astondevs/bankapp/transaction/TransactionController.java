package ru.astondevs.bankapp.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.transaction.dto.DepositCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionDto;
import ru.astondevs.bankapp.transaction.dto.WithdrawCreationDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/transactions")
    public TransactionDto create(@RequestBody @Valid TransactionCreationDto transactionCreationDto) {
        log.info("Received create transaction request with body: {}", transactionCreationDto);
        return service.create(transactionCreationDto);
    }

    @PostMapping("/accounts/{accountId}/deposit")
    public AccountDto deposit(@PathVariable Long accountId,
                              @RequestBody @Valid DepositCreationDto depositCreationDto) {
        log.info("Received deposit request from account with id {} and body: {}", accountId, depositCreationDto);
        return service.deposit(accountId, depositCreationDto);
    }

    @PostMapping("/accounts/{accountId}/withdraw")
    public AccountDto withdraw(@PathVariable Long accountId,
                               @RequestBody @Valid WithdrawCreationDto withdrawCreationDto) {
        log.info("Received withdraw request from account with id {} and body: {}", accountId, withdrawCreationDto);
        return service.withdraw(accountId, withdrawCreationDto);
    }
}
