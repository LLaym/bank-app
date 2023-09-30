package ru.astondevs.bankapp.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.astondevs.bankapp.account.dto.AccountCreationDto;
import ru.astondevs.bankapp.account.dto.AccountDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mvc;

    @Test
    void create_whenAccountCreationDtoValid_thenResponseStatusOkWithAccountDtoInBody() throws Exception {
        AccountCreationDto accountCreationDto = new AccountCreationDto("Test", 1234, 1000.0);
        AccountDto expectedResult = new AccountDto("Test", 1000.0);

        when(accountService.create(any(AccountCreationDto.class)))
                .thenReturn(expectedResult);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));

        verify(accountService, times(1)).create(any(AccountCreationDto.class));
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void create_whenOwnerNotValid_thenResponseStatusClientError() throws Exception {
        AccountCreationDto accountCreationDto = new AccountCreationDto(null, 1234, 1000.0);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        AccountCreationDto accountCreationDto2 = new AccountCreationDto("T", 1234, 1000.0);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        AccountCreationDto accountCreationDto3 = new AccountCreationDto(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam id nunc auctor, aliquam nunc" +
                " id, ultricies nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacinia ultrices, nisl justo lacinia nunc, nec tincidunt" +
                " nunc nunc id nunc. Sed euismod, velit nec lacin", 1234, 1000.0);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(accountService);
    }

    @Test
    void create_whenPinNotValid_thenResponseStatusClientError() throws Exception {
        AccountCreationDto accountCreationDto = new AccountCreationDto("Test", null, 1000.0);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        AccountCreationDto accountCreationDto2 = new AccountCreationDto("Test", 123, 1000.0);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        AccountCreationDto accountCreationDto3 = new AccountCreationDto("Test", 12345, 1000.0);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(accountService);
    }

    @Test
    void create_whenBalanceNotValid_thenResponseStatusClientError() throws Exception {
        AccountCreationDto accountCreationDto = new AccountCreationDto("Test", null, null);

        mvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(accountCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(accountService);
    }

    @Test
    void getById_whenInvoke_thenResponseStatusOkWithAccountDtoInBody() throws Exception {
        Long accountId = 1L;
        AccountDto expectedResult = new AccountDto("Test", 1000.0);

        when(accountService.getById(anyLong()))
                .thenReturn(expectedResult);

        mvc.perform(get("/accounts/{accountId}", accountId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));

        verify(accountService, times(1)).getById(anyLong());
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void get_whenInvoke_thenResponseStatusOkWithListOfAccountDtoInBody() throws Exception {
        Integer from = 0;
        Integer size = 10;
        List<AccountDto> expectedResult = List.of(new AccountDto("Test", 1000.0));

        when(accountService.get(anyInt(), anyInt()))
                .thenReturn(expectedResult);

        mvc.perform(get("/accounts")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));

        verify(accountService, times(1)).get(anyInt(), anyInt());
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void get_whenParamsNotValid_thenResponseStatusClientError() throws Exception {
        Integer from = -1;
        Integer size = 10;

        mvc.perform(get("/accounts")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Integer from2 = 0;
        Integer size2 = 0;

        mvc.perform(get("/accounts")
                        .param("from", String.valueOf(from2))
                        .param("size", String.valueOf(size2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(accountService);
    }
}