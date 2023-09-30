package ru.astondevs.bankapp.transaction.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.bankapp.account.AccountMapper;
import ru.astondevs.bankapp.account.AccountRepository;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.account.model.Account;
import ru.astondevs.bankapp.transaction.TransactionMapper;
import ru.astondevs.bankapp.transaction.TransactionRepository;
import ru.astondevs.bankapp.transaction.dto.DepositCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionDto;
import ru.astondevs.bankapp.transaction.dto.WithdrawCreationDto;
import ru.astondevs.bankapp.transaction.model.Transaction;
import ru.astondevs.bankapp.util.exception.NoAccessException;
import ru.astondevs.bankapp.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @Captor
    ArgumentCaptor<Account> accountArgumentCaptor;
    @Captor
    ArgumentCaptor<Transaction> transactionArgumentCaptor;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private AccountMapper accountMapper;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account account1;
    private Account account2;

    @BeforeEach
    void addData() {
        account1 = new Account(1L, "Test1", 1234, 100.0);
        account2 = new Account(2L, "Test2", 4321, 100.0);
    }

    @Test
    void create_whenAllValid_thenCreateAndTransactionDtoReturned() {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(1L, 2L, 20.0, 1234);
        TransactionDto expectedResult = new TransactionDto(1L, 2L, 20.0, LocalDateTime.now());

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1))
                .thenReturn(Optional.of(account2));
        when(accountRepository.save(any()))
                .thenReturn(new Account())
                .thenReturn(new Account());
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(new Transaction());
        when(transactionMapper.toTransactionDto(any(Transaction.class)))
                .thenReturn(expectedResult);

        TransactionDto result = transactionService.create(transactionCreationDto);

        assertNotNull(result);

        verify(accountRepository, times(2))
                .save(accountArgumentCaptor.capture());
        verify(transactionRepository, times(1))
                .save(transactionArgumentCaptor.capture());
        verifyNoMoreInteractions(accountRepository, transactionRepository, transactionMapper);

        List<Account> updatedAccounts = accountArgumentCaptor.getAllValues();
        Transaction savedTransaction = transactionArgumentCaptor.getValue();

        assertEquals(80, updatedAccounts.get(0).getBalance());
        assertEquals(120, updatedAccounts.get(1).getBalance());

        assertEquals(account1, savedTransaction.getSender());
        assertEquals(account2, savedTransaction.getRecipient());
        assertEquals(transactionCreationDto.getAmount(), savedTransaction.getAmount());
        assertNotNull(savedTransaction.getDate());
    }

    @Test
    void create_whenNotEnoughMoney_theNoAccessExceptionThrown() {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(1L, 2L, 999999.0, 1234);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1))
                .thenReturn(Optional.of(account2));


        assertThrows(NoAccessException.class, () -> transactionService.create(transactionCreationDto));

        verify(accountRepository, times(2)).findById(anyLong());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void create_whenTransactionToSelf_theNoAccessExceptionThrown() {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(1L, 1L, 50.0, 1234);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1))
                .thenReturn(Optional.of(account1));


        assertThrows(NoAccessException.class, () -> transactionService.create(transactionCreationDto));

        verify(accountRepository, times(2)).findById(anyLong());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void create_whenPinWrong_theNoAccessExceptionThrown() {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(1L, 2L, 50.0, 5555);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1))
                .thenReturn(Optional.of(account2));


        assertThrows(NoAccessException.class, () -> transactionService.create(transactionCreationDto));

        verify(accountRepository, times(2)).findById(anyLong());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void create_whenRecipientAccountNotFound_theNoAccessExceptionThrown() {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(1L, 999L, 50.0, 5555);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1))
                .thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> transactionService.create(transactionCreationDto));

        verify(accountRepository, times(2)).findById(anyLong());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void create_whenSenderAccountNotFound_theNoAccessExceptionThrown() {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(999L, 2L, 50.0, 5555);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> transactionService.create(transactionCreationDto));

        verify(accountRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void deposit_whenAccountExist_thenAccountDtoReturned() {
        Long accountId = 1L;
        DepositCreationDto depositCreationDto = new DepositCreationDto(20.0);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1));
        when(accountRepository.save(any(Account.class)))
                .thenReturn(new Account());
        when(accountMapper.toAccountDto(any(Account.class)))
                .thenReturn(new AccountDto());

        AccountDto result = transactionService.deposit(accountId, depositCreationDto);
        assertNotNull(result);

        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());
        verify(accountMapper, times(1)).toAccountDto(any(Account.class));
        verifyNoMoreInteractions(accountRepository, accountMapper);

        Account updatedAccount = accountArgumentCaptor.getValue();
        assertEquals(120, updatedAccount.getBalance());
    }

    @Test
    void deposit_whenAccountNotExist_thenNotFoundExceptionThrown() {
        Long accountId = 1L;
        DepositCreationDto depositCreationDto = new DepositCreationDto(20.0);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.deposit(accountId, depositCreationDto));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void withdraw_whenAllValid_thenAccountDtoReturned() {
        Long accountId = 1L;
        WithdrawCreationDto withdrawCreationDto = new WithdrawCreationDto(20.0, 1234);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1));
        when(accountRepository.save(any(Account.class)))
                .thenReturn(new Account());
        when(accountMapper.toAccountDto(any(Account.class)))
                .thenReturn(new AccountDto());

        AccountDto result = transactionService.withdraw(accountId, withdrawCreationDto);
        assertNotNull(result);

        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());
        verify(accountMapper, times(1)).toAccountDto(any(Account.class));
        verifyNoMoreInteractions(accountRepository, accountMapper);

        Account updatedAccount = accountArgumentCaptor.getValue();
        assertEquals(80, updatedAccount.getBalance());
    }

    @Test
    void withdraw_whenNotEnoughMoney_thenNoAccessExceptionThrown() {
        Long accountId = 1L;
        WithdrawCreationDto withdrawCreationDto = new WithdrawCreationDto(999.0, 1234);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1));

        assertThrows(NoAccessException.class, () -> transactionService.withdraw(accountId, withdrawCreationDto));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void withdraw_whenWrongPing_thenNoAccessExceptionThrown() {
        Long accountId = 1L;
        WithdrawCreationDto withdrawCreationDto = new WithdrawCreationDto(20.0, 5555);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account1));

        assertThrows(NoAccessException.class, () -> transactionService.withdraw(accountId, withdrawCreationDto));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void withdraw_whenAccountNotExist_thenNotFoundExceptionThrown() {
        Long accountId = 999L;
        WithdrawCreationDto withdrawCreationDto = new WithdrawCreationDto(20.0, 1234);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.withdraw(accountId, withdrawCreationDto));
        verifyNoMoreInteractions(accountRepository);
    }
}