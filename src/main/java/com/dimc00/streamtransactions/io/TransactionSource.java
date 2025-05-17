package com.dimc00.streamtransactions.io;

import com.dimc00.streamtransactions.model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class TransactionSource implements AutoCloseable {
    private final BufferedReader reader;
    private String nextLine;
    private final String type; // "INCOME" или "OUTCOME"

    public TransactionSource(String filePath, String type) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
        this.type = type;
        reader.readLine(); // пропускаем заголовок
        advance();
    }

    private void advance() throws IOException {
        nextLine = reader.readLine();
    }

    public boolean hasNext() {
        return nextLine != null;
    }

    public Transaction peek() {
        if (nextLine == null) throw new NoSuchElementException("No more lines");
        String[] parts = nextLine.split(",");
        return new Transaction(
                parts[0], // transactionId
                parts[1], // customerId
                parts[2], // accountId
                Double.parseDouble(parts[3]), // amount
                LocalDateTime.parse(parts[4]), // dateTime
                type // INCOME / OUTCOME
        );
    }

    public Transaction next() throws IOException {
        Transaction tx = peek();
        advance();
        return tx;
    }

    public void close() throws IOException {
        reader.close();
    }
}