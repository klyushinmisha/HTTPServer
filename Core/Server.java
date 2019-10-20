package com.mikhail.ksp.HTTPServer.Core;

import com.mikhail.ksp.HTTPServer.Utils.ServerUrlBind;
import com.mikhail.ksp.HTTPServer.Utils.URLHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private int port;
    private HashMap<String, ServerUrlBind> urlBindings = new HashMap<>();

    public Server(int port) {
        this.port = port;
    }

    public void bindUrlHandler(String url, String method, URLHandler handler) {
        urlBindings.put(url, new ServerUrlBind(method, handler));
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(this.port)) {
            while (true) {
                Socket client = server.accept();
                Worker worker = new Worker(client, urlBindings);
                new Thread(worker).start();
            }
        } catch (IOException e) {
            System.out.println("Ошибка подключения");
        }
    }
}
