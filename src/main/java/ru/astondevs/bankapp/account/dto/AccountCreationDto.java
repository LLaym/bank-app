package ru.astondevs.bankapp.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationDto {
    @NotBlank(message = "Поле: owner. Ошибка: поле не может быть пустым")
    @Size(min = 2, max = 512, message = "Поле: owner. Ошибка: длина строки должна быть от 2 до 512 символов")
    private String owner;

    @NotNull(message = "Поле: pin. Ошибка: не может быть null")
    @Min(value = 1000, message = "Поле: pin. Ошибка: pin должен состоять из 4-х цифр")
    @Max(value = 9999, message = "Поле: pin. Ошибка: pin должен состоять из 4-х== цифр")
    private Integer pin;

    @NotNull(message = "Поле: balance. Ошибка: не может быть null")
    private Double balance;
}
