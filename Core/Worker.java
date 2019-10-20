package com.mikhail.ksp.HTTPServer.Core;

import com.mikhail.ksp.HTTPServer.Utils.ServerUrlBind;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class Worker implements Runnable {

    private Socket client;
    private HashMap<String, ServerUrlBind> urlBindings;

    public Worker(Socket client, HashMap<String, ServerUrlBind> urlBindings) {
        this.client = client;
        this.urlBindings = urlBindings;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
            );

            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(client.getOutputStream())
            );
            try {
                String recStr = null;
                String httpData = "";
                while (in.ready()) {
                    recStr = in.readLine();
                    if (recStr == null) {
                        break;
                    }
                    httpData += recStr + "\n";
                }
                if (httpData.isEmpty()) {
                    return;
                }
                HTTPRequest request = new HTTPRequest();
                request.parseData(httpData);

                HTTPResponse response;


                if (request.getQueryParams() == null) {
                    response = this.handleUnprocessableEntity();
                    response.setVersion(request.getVersion());
                    out.write(response.parseMetadata());
                    out.flush();
                    return;
                }

                if (!urlBindings.containsKey(request.getUrl())) {
                    response = this.handleNotFound();
                    response.setVersion(request.getVersion());
                    out.write(response.parseMetadata());
                    out.flush();
                    return;
                }
                ServerUrlBind binding = urlBindings.get(request.getUrl());
                if (request.getMethod().compareTo(binding.getMethod()) != 0) {
                    response = this.handleMethodNotAllowed();
                    response.setVersion(request.getVersion());
                    out.write(response.parseMetadata());
                    out.flush();
                    return;
                }
                response = binding.getHandler().handleRequest(request);
                response.setVersion(request.getVersion());
                out.write(response.parseMetadata());
                out.flush();
                client.getOutputStream().write(response.getBody());
            } catch (IOException e) {

            } finally {
                client.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {

        }
    }

    private HTTPResponse handleMethodNotAllowed() {
        HTTPResponse response = new HTTPResponse();
        response.setStatus(403);
        return response;
    }

    private HTTPResponse handleNotFound() {
        HTTPResponse response = new HTTPResponse();
        response.setStatus(404);
        return response;
    }

    private HTTPResponse handleUnprocessableEntity() {
        HTTPResponse response = new HTTPResponse();
        response.setStatus(422);
        return response;
    }
}
