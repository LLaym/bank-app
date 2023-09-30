package ru.astondevs.bankapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreationDto {
    @NotNull(message = "Поле: sender. Ошибка: не может быть null")
    private Long sender;

    @NotNull(message = "Поле: recipient. Ошибка: не может быть null")
    private Long recipient;

    @NotNull(message = "Поле: amount. Ошибка: не может быть null")
    @Min(value = 1, message = "Поле: amount. Ошибка: не может быть меньше 1")
    private Double amount;

    @NotNull(message = "Поле: pin. Ошибка: не может быть null")
    @Min(value = 1000, message = "Поле: pin. Ошибка: pin должен состоять из 4-х цифр")
    @Max(value = 9999, message = "Поле: pin. Ошибка: pin должен состоять из 4-х== цифр")
    private Integer pin;
}
