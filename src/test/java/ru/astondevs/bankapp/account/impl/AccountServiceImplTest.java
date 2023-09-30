package ru.astondevs.bankapp.account.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.astondevs.bankapp.account.AccountMapper;
import ru.astondevs.bankapp.account.AccountRepository;
import ru.astondevs.bankapp.account.dto.AccountCreationDto;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.account.model.Account;
import ru.astondevs.bankapp.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Captor
    ArgumentCaptor<Account> captor;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void create_whenInvoke_thenCreateAndAccountDtoReturned() {
        AccountCreationDto accountCreationDto = new AccountCreationDto("Test", 1234, 100.0);

        when(accountRepository.save(any(Account.class)))
                .thenReturn(new Account());
        when(accountMapper.toAccountDto(any(Account.class)))
                .thenReturn(new AccountDto());

        AccountDto result = accountService.create(accountCreationDto);

        assertNotNull(result);

        verify(accountRepository, times(1)).save(captor.capture());
        verifyNoMoreInteractions(accountRepository);

        Account savedAccount = captor.getValue();
        assertEquals(accountCreationDto.getOwner(), savedAccount.getOwner());
        assertEquals(accountCreationDto.getPin(), savedAccount.getPin());
        assertEquals(accountCreationDto.getBalance(), savedAccount.getBalance());
    }

    @Test
    void getById_whenAccountFound_thenAccountDtoReturned() {
        Long accountId = 1L;

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Account()));
        when(accountMapper.toAccountDto(any(Account.class)))
                .thenReturn(new AccountDto());

        AccountDto result = accountService.getById(accountId);
        assertNotNull(result);
    }

    @Test
    void getById_whenAccountNotFound_thenAccountDtoReturned() {
        Long accountId = 1L;

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.getById(accountId));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void get_whenInvoke_thenListOfAccountDtoReturned() {
        Integer from = 0;
        Integer size = 10;

        when(accountRepository.findAllBy(any(Pageable.class)))
                .thenReturn(List.of(new Account()));
        when(accountMapper.toAccountDto(any(Account.class)))
                .thenReturn(new AccountDto());

        List<AccountDto> result = accountService.get(from, size);
        assertNotNull(result);
    }
}