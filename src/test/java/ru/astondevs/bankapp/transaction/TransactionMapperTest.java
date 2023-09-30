package ru.astondevs.bankapp.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.bankapp.account.model.Account;
import ru.astondevs.bankapp.transaction.dto.TransactionDto;
import ru.astondevs.bankapp.transaction.model.Transaction;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TransactionMapperTest {
    @InjectMocks
    private TransactionMapper transactionMapper;

    @Test
    void toAccountDto_whenInvoke_thenAccountDtoReturned() {
        Account sender = new Account(1L, "Test", 1234, 100.0);
        Account recipient = new Account(2L, "Test2", 4321, 200.0);
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(1L, sender, recipient, 1000.0, date);

        TransactionDto result = transactionMapper.toTransactionDto(transaction);

        assertNotNull(result);
        assertEquals(transaction.getSender().getId(), result.getSender());
        assertEquals(transaction.getRecipient().getId(), result.getRecipient());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getDate(), result.getDate());
    }
}