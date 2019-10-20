package com.mikhail.ksp.HTTPServer.Core;

import java.util.HashMap;

public class HTTPResponse {
    private String version;
    private int status;
    private String statusMessage;

    private HashMap<String, String> headers = new HashMap<>();

    private String body = "";

    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatus(int status) {
        this.status = status;
        switch (status) {
            case 200:
                this.statusMessage = "OK";
                break;
            case 403:
                this.statusMessage = "Method Not Allowed";
                break;
            case 404:
                this.statusMessage = "Not Found";
                break;
        }
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String parseData() {
        final StringBuilder data = new StringBuilder(
                String.join(" ", version, String.valueOf(status), statusMessage) + "\n"
        );
        headers.forEach((k, v) -> {
            data.append(k + ": " + v + "\n");
        });
        data.append("\n");
        data.append(body);
        return data.toString();
    }
}
