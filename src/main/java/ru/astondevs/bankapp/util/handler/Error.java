package ru.astondevs.bankapp.util.handler;

import lombok.Value;

@Value
public class Error {
    String status;
    String reason;
    String message;
    String timestamp;
}
