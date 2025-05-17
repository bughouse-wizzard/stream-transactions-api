package com.dimc00.streamtransactions.http;

import com.dimc00.streamtransactions.model.Transaction;
import com.dimc00.streamtransactions.service.TransactionService;
import com.dimc00.streamtransactions.util.QueryUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TransactionHandler implements HttpHandler {

    private final TransactionService service;
    private final ObjectMapper objectMapper;

    public TransactionHandler() {
        this.service = new TransactionService("incomes.csv", "outcomes.csv");
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            Map<String, String> query = QueryUtils.parse(exchange.getRequestURI().getRawQuery());

            String accountId = query.get("accountId");
            LocalDate date = query.containsKey("date") ? LocalDate.parse(query.get("date")) : null;
            int offset = Integer.parseInt(query.getOrDefault("offset", "0"));
            int limit = Integer.parseInt(query.getOrDefault("limit", "20"));

            List<Transaction> transactions = service.getTransactions(accountId, date, offset, limit);
            String json = objectMapper.writeValueAsString(transactions);

            byte[] response = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
            String error = "{\"error\":\"Internal server error\"}";
            try {
                exchange.sendResponseHeaders(500, error.length());
                exchange.getResponseBody().write(error.getBytes(StandardCharsets.UTF_8));
                exchange.getResponseBody().close();
            } catch (Exception ignored) {}
        }
    }
}