package com.dimc00.streamtransactions.service;

import com.dimc00.streamtransactions.io.TransactionSource;
import com.dimc00.streamtransactions.model.Transaction;

import java.time.LocalDate;
import java.util.*;

public class TransactionService {

    private final String incomesPath;
    private final String outcomesPath;

    public TransactionService(String incomesPath, String outcomesPath) {
        this.incomesPath = incomesPath;
        this.outcomesPath = outcomesPath;
    }

    public List<Transaction> getTransactions(String accountId, LocalDate date, int offset, int limit) throws Exception {
        List<Transaction> result = new ArrayList<>();

        try (
                TransactionSource incomes = new TransactionSource(incomesPath, "INCOME");
                TransactionSource outcomes = new TransactionSource(outcomesPath, "OUTCOME")
        ) {
            Transaction in = incomes.hasNext() ? incomes.next() : null;
            Transaction out = outcomes.hasNext() ? outcomes.next() : null;

            int skipped = 0;

            while ((in != null || out != null) && result.size() < limit) {
                Transaction next;

                if (in == null) {
                    next = out;
                    out = outcomes.hasNext() ? outcomes.next() : null;
                } else if (out == null) {
                    next = in;
                    in = incomes.hasNext() ? incomes.next() : null;
                } else if (in.dateTime.isBefore(out.dateTime)) {
                    next = in;
                    in = incomes.hasNext() ? incomes.next() : null;
                } else {
                    next = out;
                    out = outcomes.hasNext() ? outcomes.next() : null;
                }

                boolean matches = true;

                if (accountId != null && !next.accountId.equals(accountId)) {
                    matches = false;
                }
                if (date != null && !next.dateTime.toLocalDate().equals(date)) {
                    matches = false;
                }

                if (matches) {
                    if (skipped < offset) {
                        skipped++;
                    } else {
                        result.add(next);
                    }
                }
            }
        }

        return result;
    }
}