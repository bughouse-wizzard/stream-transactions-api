package com.dimc00.streamtransactions.model;

import java.time.LocalDateTime;

public class Transaction {
    public String transactionId;
    public String customerId;
    public String accountId;
    public double amount;
    public LocalDateTime dateTime;
    public String type; // "INCOME" or "OUTCOME"

    public Transaction(String transactionId, String customerId, String accountId,
                       double amount, LocalDateTime dateTime, String type) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.accountId = accountId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.type = type;
    }

    // нужны геттеры для сериализации через Jackson (если будешь делать private-поля)
}