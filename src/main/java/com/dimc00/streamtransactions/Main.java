package com.dimc00.streamtransactions;

import com.dimc00.streamtransactions.http.TransactionHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/transactions", new TransactionHandler());

        // 💡 Виртуальные потоки (Java 21)
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        server.start();
        System.out.println("🚀 Server started at http://localhost:8080/transactions");
    }
}