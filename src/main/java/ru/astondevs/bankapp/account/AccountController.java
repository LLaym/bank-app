package ru.astondevs.bankapp.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.astondevs.bankapp.account.dto.AccountCreationDto;
import ru.astondevs.bankapp.account.dto.AccountDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService service;

    @PostMapping("/accounts")
    public AccountDto create(@RequestBody @Valid AccountCreationDto accountCreationDto) {
        log.info("Received creation account request with body: {}", accountCreationDto);
        return service.create(accountCreationDto);
    }

    @GetMapping("/accounts/{accountId}")
    public AccountDto getById(@PathVariable Long accountId) {
        log.info("Received get account with id {} request", accountId);
        return service.getById(accountId);
    }

    @GetMapping("/accounts")
    public List<AccountDto> get(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Received get all accounts request with from {} and size {} params", from, size);
        return service.get(from, size);
    }
}
