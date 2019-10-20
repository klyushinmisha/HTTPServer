package com.mikhail.ksp.HTTPServer.Core;

import java.util.HashMap;

public class HTTPRequest {
    private enum HTTPLineType {
        START_LINE,
        HEADER,
        BODY
    }

    private HTTPLineType lineType;

    private String method;
    private String url;
    private String version;

    private HashMap<String, String> headers = new HashMap<>();

    private String body;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public HTTPRequest() {
        this.lineType = HTTPLineType.START_LINE;
    }

    private void parseStartLine(String startLine) {
        String[] data = startLine.split(" ", 3);
        this.setMethod(data[0]);
        this.setUrl(data[1]);
        this.setVersion(data[2]);
    }

    private void parseHeader(String header) {
        String[] data = header.split(": ", 2);
        this.setHeader(data[0], data[1]);
    }

    public void parseData(String httpStrData) {
        String[] httpLines = httpStrData.split("\n");
        String body = new String();
        for (String line : httpLines) {
            switch (this.lineType) {
                case START_LINE:
                    parseStartLine(line);
                    this.lineType = HTTPLineType.HEADER;
                    break;
                case HEADER:
                    if (line.trim().isEmpty()) {
                        this.lineType = HTTPLineType.BODY;
                        continue;
                    }
                    parseHeader(line);
                    break;
                case BODY:
                    body += line;
                    break;
            }
        }
        this.setBody(body);
    }
}
