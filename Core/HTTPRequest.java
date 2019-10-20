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
    private HashMap<String, String> queryParams;
    private String version;

    private HashMap<String, String> headers = new HashMap<>();

    private String body;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setQueryParams(HashMap<String, String> queryParams) {
        this.queryParams = queryParams;
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

    public HashMap<String, String> getQueryParams() {
        return queryParams;
    }

    public String getVersion() {
        return version;
    }

    public HTTPRequest() {
        this.lineType = HTTPLineType.START_LINE;
    }

    private void parseStartLine(String startLine) {
        String[] data = startLine.split(" ", 3);
        this.setMethod(data[0]);

        String url = data[1];
        String[] urlData = url.split("\\?", 2);
        url = urlData[0];
        String queryParams = null;
        if (urlData.length == 2) {
            queryParams = urlData[1];
        }

        HashMap<String, String> paramsMap = parseQueryParams(queryParams);
        this.setUrl(url);
        this.setQueryParams(paramsMap);
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

    private HashMap<String, String> parseQueryParams(String queryParams) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (queryParams == null) {
            return paramsMap;
        }
        for (String qp : queryParams.split("&")) {
            String[] paramData = qp.split("=", 2);
            for (String str : paramData) {
                if (str.compareTo("") == 0) {
                    return null;
                }
            }
            if (paramData.length != 2) {
                return null;
            }
            paramsMap.put(paramData[0], paramData[1]);
        }
        return paramsMap;
    }
}
