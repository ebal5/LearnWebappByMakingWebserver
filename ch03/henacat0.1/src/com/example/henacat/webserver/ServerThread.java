package com.example.henacat.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.example.henacat.servletimpl.ServletService;
import com.example.henacat.servletimpl.WebApplication;
import com.example.henacat.util.Constants;
import com.example.henacat.util.MyURLDecoder;
import com.example.henacat.util.SendResponse;
import com.example.henacat.util.Util;

public class ServerThread implements Runnable {
    private static final String DOCUMENT_ROOT = "/workspace/ch03/htdocs";
    private static final String ERROR_DOCUMENT = "/workspace/ch03/htdocs/error";
    private Socket socket;

    private static void addRequestHeader(Map<String, String> reqHeader, String line) {
        var colonPos = line.indexOf(":");
        if (colonPos == -1)
            return;

        var headerName = line.substring(0, colonPos).toUpperCase();
        var headerValue = line.substring(colonPos + 1).trim();
        reqHeader.put(headerName, headerValue);
    }

    @Override
    public void run() {
        OutputStream output = null;
        try {
            InputStream input = socket.getInputStream();

            String line;
            String requestLine = null;
            String method = null;
            var reqHeader = new HashMap<String, String>();
            while ((line = Util.readLine(input)) != null) {
                if (line == "")
                    break;
                if (line.startsWith("GET")) {
                    method = "GET";
                    requestLine = line;
                } else if (line.startsWith("POST")) {
                    method = "POST";
                    requestLine = line;
                } else {
                    addRequestHeader(reqHeader, line);
                }
            }

            if (requestLine == null)
                return;

            var reqUri = MyURLDecoder.decode(requestLine.split(" ")[1], "UTF-8");

            var pathAndQuery = reqUri.split("\\?");
            var path = pathAndQuery[0];
            String query = null;
            if (pathAndQuery.length > 1) {
                query = pathAndQuery[1];
            }

            output = new BufferedOutputStream(socket.getOutputStream());

            var appDir = path.substring(1).split("/")[0];
            var webApp = WebApplication.searchWebApplication(appDir);
            if (webApp != null) {
                var servletInfo = webApp.searchServlet(path.substring(appDir.length() + 1));
                if (servletInfo != null) {
                    ServletService.doService(method, query, servletInfo, reqHeader, input, output);
                    return;
                }
            }

            String ext = null;
            var tmp = reqUri.split("\\.");
            ext = tmp[tmp.length - 1];

            if (path.endsWith("/")) {
                path += "index.html";
                ext = "html";
            }

            var fs = FileSystems.getDefault();
            var pathObj = fs.getPath(DOCUMENT_ROOT + path);
            Path realPath;
            try {
                realPath = pathObj.toRealPath();
            } catch (NoSuchFileException e) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            }
            if (!realPath.startsWith(DOCUMENT_ROOT)) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            } else if (Files.isDirectory(realPath)) {
                var host = reqHeader.get("HOST");
                var location = "http://" + ((host != null) ? host : Constants.SERVER_NAME) + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
                return;
            }
            try (var fis = new BufferedInputStream(Files.newInputStream(realPath))) {
                SendResponse.sendOkResponse(output, fis, ext);
            } catch (FileNotFoundException e) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
    }
}
