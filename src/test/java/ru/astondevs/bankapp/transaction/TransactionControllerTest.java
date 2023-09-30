package ru.astondevs.bankapp.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.astondevs.bankapp.account.dto.AccountDto;
import ru.astondevs.bankapp.transaction.dto.DepositCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionCreationDto;
import ru.astondevs.bankapp.transaction.dto.TransactionDto;
import ru.astondevs.bankapp.transaction.dto.WithdrawCreationDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private TransactionService transactionService;
    @Autowired
    private MockMvc mvc;

    @Test
    void create_whenTransactionCreationDtoValid_thenResponseStatusOkWithTransactionDtoInBody() throws Exception {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(1L, 2L, 100.0, 1234);
        TransactionDto transactionDto =
                new TransactionDto(1L, 2L, 100.0, LocalDateTime.now());

        when(transactionService.create(any(TransactionCreationDto.class)))
                .thenReturn(transactionDto);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(transactionDto)));

        verify(transactionService, times(1)).create(any(TransactionCreationDto.class));
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void create_whenTransactionCreationDtoNotValid_thenResponseStatusClientError() throws Exception {
        TransactionCreationDto transactionCreationDto =
                new TransactionCreationDto(null, 2L, 100.0, 1234);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        TransactionCreationDto transactionCreationDto2 =
                new TransactionCreationDto(1L, null, 100.0, 1234);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        TransactionCreationDto transactionCreationDto3 =
                new TransactionCreationDto(1L, 2L, null, 1234);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        TransactionCreationDto transactionCreationDto4 =
                new TransactionCreationDto(1L, 2L, 0.0, 1234);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto4))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        TransactionCreationDto transactionCreationDto5 =
                new TransactionCreationDto(1L, 2L, 100.0, null);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto5))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        TransactionCreationDto transactionCreationDto6 =
                new TransactionCreationDto(1L, 2L, 100.0, 123);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto6))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        TransactionCreationDto transactionCreationDto7 =
                new TransactionCreationDto(1L, 2L, 100.0, 12345);

        mvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(transactionCreationDto7))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(transactionService);
    }

    @Test
    void deposit_whenDepositCreationDtoValid_thenResponseStatusOkWithAccountDtoInBody() throws Exception {
        Long accountId = 1L;
        DepositCreationDto depositCreationDto = new DepositCreationDto(100.0);
        AccountDto expectedResult = new AccountDto("Test", 900.0);

        when(transactionService.deposit(anyLong(), any(DepositCreationDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(post("/accounts/{accountId}/deposit", accountId)
                        .content(mapper.writeValueAsString(depositCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));

        verify(transactionService, times(1)).deposit(anyLong(), any(DepositCreationDto.class));
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void deposit_whenDepositCreationDtoNotValid_thenResponseStatusOkWithAccountDtoInBody() throws Exception {
        Long accountId = 1L;
        DepositCreationDto depositCreationDto = new DepositCreationDto(null);

        mvc.perform(post("/accounts/{accountId}/deposit", accountId)
                        .content(mapper.writeValueAsString(depositCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        DepositCreationDto depositCreationDto2 = new DepositCreationDto(0.0);

        mvc.perform(post("/accounts/{accountId}/deposit", accountId)
                        .content(mapper.writeValueAsString(depositCreationDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(transactionService);
    }

    @Test
    void withdraw_whenWithdrawCreationDtoValid_thenResponseStatusOkWithAccountDtoInBody() throws Exception {
        Long accountId = 1L;
        WithdrawCreationDto withdrawCreationDto = new WithdrawCreationDto(100.0, 1234);
        AccountDto expectedResult = new AccountDto("Test", 900.0);

        when(transactionService.withdraw(anyLong(), any(WithdrawCreationDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(post("/accounts/{accountId}/withdraw", accountId)
                        .content(mapper.writeValueAsString(withdrawCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));

        verify(transactionService, times(1))
                .withdraw(anyLong(), any(WithdrawCreationDto.class));
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void withdraw_whenWithdrawCreationDtoNotValid_thenResponseStatusOkWithAccountDtoInBody() throws Exception {
        Long accountId = 1L;
        WithdrawCreationDto withdrawCreationDto = new WithdrawCreationDto(null, 1234);

        mvc.perform(post("/accounts/{accountId}/withdraw", accountId)
                        .content(mapper.writeValueAsString(withdrawCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        WithdrawCreationDto withdrawCreationDto2 = new WithdrawCreationDto(0.0, 1234);

        mvc.perform(post("/accounts/{accountId}/withdraw", accountId)
                        .content(mapper.writeValueAsString(withdrawCreationDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        WithdrawCreationDto withdrawCreationDto3 = new WithdrawCreationDto(100.0, null);

        mvc.perform(post("/accounts/{accountId}/withdraw", accountId)
                        .content(mapper.writeValueAsString(withdrawCreationDto3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        WithdrawCreationDto withdrawCreationDto4 = new WithdrawCreationDto(100.0, 123);

        mvc.perform(post("/accounts/{accountId}/withdraw", accountId)
                        .content(mapper.writeValueAsString(withdrawCreationDto4))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        WithdrawCreationDto withdrawCreationDto5 = new WithdrawCreationDto(100.0, 12345);

        mvc.perform(post("/accounts/{accountId}/withdraw", accountId)
                        .content(mapper.writeValueAsString(withdrawCreationDto5))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(transactionService);
    }
}