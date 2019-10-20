package com.mikhail.ksp.HTTPServer.Utils;

import com.mikhail.ksp.HTTPServer.Core.HTTPRequest;
import com.mikhail.ksp.HTTPServer.Core.HTTPResponse;

public interface URLHandler {
    HTTPResponse handleRequest(HTTPRequest data);
}
