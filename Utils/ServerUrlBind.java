package com.mikhail.ksp.HTTPServer.Utils;

public class ServerUrlBind {
    private String method;
    private URLHandler handler;

    public ServerUrlBind(String method, URLHandler handler) {
        this.method = method;
        this.handler = handler;
    }

    public String getMethod() {
        return method;
    }

    public URLHandler getHandler() {
        return handler;
    }
}