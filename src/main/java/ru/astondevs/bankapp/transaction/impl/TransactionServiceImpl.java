package ru.astondevs.bankapp.transaction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.astondevs.bankapp.account.AccountMapper;
import ru.astondevs.bankapp.account.AccountRepository;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.account.model.Account;
import ru.astondevs.bankapp.transaction.TransactionMapper;
import ru.astondevs.bankapp.transaction.TransactionRepository;
import ru.astondevs.bankapp.transaction.TransactionService;
import ru.astondevs.bankapp.transaction.dto.DepositCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionDto;
import ru.astondevs.bankapp.transaction.dto.WithdrawCreationDto;
import ru.astondevs.bankapp.transaction.model.Transaction;
import ru.astondevs.bankapp.util.exception.NoAccessException;
import ru.astondevs.bankapp.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final AccountRepository accountRepository;
    private final TransactionMapper mapper;
    private final AccountMapper accountMapper;

    @Transactional
    @Override
    public TransactionDto create(TransactionCreationDto transactionCreationDto) {
        Double transactionAmount = transactionCreationDto.getAmount();

        Account sender = accountRepository.findById(transactionCreationDto.getSender())
                .orElseThrow(() -> new NotFoundException(
                        "Аккаунт с id " + transactionCreationDto.getSender() + " не найден"));
        Account recipient = accountRepository.findById(transactionCreationDto.getRecipient())
                .orElseThrow(() -> new NotFoundException(
                        "Аккаунт с id " + transactionCreationDto.getRecipient() + " не найден"));

        if (!Objects.equals(sender.getPin(), transactionCreationDto.getPin())) {
            throw new NoAccessException("Неправильный PIN");
        }
        if (Objects.equals(sender.getId(), recipient.getId())) {
            throw new NoAccessException("Невозможно перевести на собственный счет");
        }
        if (sender.getBalance() < transactionAmount) {
            throw new NoAccessException("Недостаточно средств");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setAmount(transactionAmount);
        transaction.setDate(LocalDateTime.now());

        sender.setBalance(sender.getBalance() - transactionAmount);
        recipient.setBalance(recipient.getBalance() + transactionAmount);

        accountRepository.save(sender);
        accountRepository.save(recipient);

        return mapper.toTransactionDto(repository.save(transaction));
    }

    @Transactional
    @Override
    public AccountDto deposit(Long accountId, DepositCreationDto depositCreationDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Аккаунт с id " + accountId + " не найден"));

        account.setBalance(account.getBalance() + depositCreationDto.getAmount());
        Account updatedAccount = accountRepository.save(account);

        return accountMapper.toAccountDto(updatedAccount);
    }

    @Transactional
    @Override
    public AccountDto withdraw(Long accountId, WithdrawCreationDto withdrawCreationDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Аккаунт с id " + accountId + " не найден"));

        if (!Objects.equals(account.getPin(), withdrawCreationDto.getPin())) {
            throw new NoAccessException("Неправильный PIN");
        }
        if (account.getBalance() < withdrawCreationDto.getAmount()) {
            throw new NoAccessException("Недостаточно средств");
        }

        account.setBalance(account.getBalance() - withdrawCreationDto.getAmount());
        Account updatedAccount = accountRepository.save(account);

        return accountMapper.toAccountDto(updatedAccount);
    }
}
