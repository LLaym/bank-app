package ru.astondevs.bankapp.account.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.astondevs.bankapp.account.AccountMapper;
import ru.astondevs.bankapp.account.AccountRepository;
import ru.astondevs.bankapp.account.AccountService;
import ru.astondevs.bankapp.account.dto.AccountCreationDto;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.account.model.Account;
import ru.astondevs.bankapp.util.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Transactional
    @Override
    public AccountDto create(AccountCreationDto accountCreationDto) {
        Account account = new Account();
        account.setOwner(accountCreationDto.getOwner());
        account.setPin(accountCreationDto.getPin());
        account.setBalance(accountCreationDto.getBalance());

        Account createdAccount = repository.save(account);

        return mapper.toAccountDto(createdAccount);
    }

    @Override
    public AccountDto getById(Long accountId) {
        Account account = repository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Category with id " + accountId + " not found"));

        return mapper.toAccountDto(account);
    }

    @Override
    public List<AccountDto> get(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, Sort.unsorted());

        List<Account> accounts = repository.findAllBy(pageRequest);

        return accounts.stream()
                .map(mapper::toAccountDto)
                .collect(Collectors.toList());
    }
}
