package ru.astondevs.bankapp.transaction;

import org.springframework.stereotype.Service;
import ru.astondevs.bankapp.transaction.dto.TransactionDto;
import ru.astondevs.bankapp.transaction.model.Transaction;

@Service
public class TransactionMapper {

    public TransactionDto toTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSender(transaction.getSender().getId());
        transactionDto.setRecipient(transaction.getRecipient().getId());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setDate(transaction.getDate());
        return transactionDto;
    }
}
