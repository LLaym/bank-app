package ru.astondevs.bankapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepositCreationDto {
    @NotNull(message = "Поле: amount. Ошибка: не может быть null")
    @Min(value = 1, message = "Поле: amount. Ошибка: не может быть меньше 1")
    private Double amount;
}