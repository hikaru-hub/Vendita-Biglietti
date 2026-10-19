package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT=3000;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            while (true) {
                Socket s = ss.accept();
                System.out.println("Connessione riuscita");
                threadPool.execute(new SocketHandler(s));
            }
        } catch (Exception e) {
            System.err.println("Errore nel server");
        } finally {
            threadPool.shutdown();
        }
    }
}