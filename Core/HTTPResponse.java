package com.mikhail.ksp.HTTPServer.Core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            case 400:
                this.statusMessage = "Bad Request";
                break;
            case 403:
                this.statusMessage = "Method Not Allowed";
                break;
            case 404:
                this.statusMessage = "Not Found";
                break;
            case 422:
                this.statusMessage = "Unprocessable Entity";
                break;
        }
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHtmlBody(String uri) {
        String htmlBody = "";
        try {
            htmlBody = Files.readString(
                    Paths.get(uri),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {

        }
        setBody(htmlBody);
    }

    public String parseMetadata() {
        final StringBuilder data = new StringBuilder(
                String.join(" ", version, String.valueOf(status), statusMessage) + "\n"
        );
        setDefaultHeaders(headers);
        headers.forEach((k, v) -> {
            data.append(k + ": " + v + "\n");
        });
        data.append("\n");
        return data.toString();
    }

    public byte[] getBody() {
        byte[] byteBody = new byte[0];
        try {
            byteBody = this.body.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return byteBody;
    }

    private void setDefaultHeaders(HashMap<String, String> headers) {
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "text/html; charset=utf-8");
        }
        headers.put("Content-Length", String.valueOf(getBody().length));
    }
}
