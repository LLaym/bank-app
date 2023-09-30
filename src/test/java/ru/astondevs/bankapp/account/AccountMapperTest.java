package ru.astondevs.bankapp.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.account.model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AccountMapperTest {
    @InjectMocks
    private AccountMapper accountMapper;

    @Test
    void toAccountDto_whenInvoke_thenAccountDtoReturned() {
        Account account = new Account(1L, "Test", 1234, 1000.0);

        AccountDto result = accountMapper.toAccountDto(account);

        assertNotNull(result);
        assertEquals(account.getOwner(), result.getOwner());
        assertEquals(account.getBalance(), result.getBalance());
    }
}