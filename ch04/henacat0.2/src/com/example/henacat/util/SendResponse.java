package com.example.henacat.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SendResponse {

    public static void sendNotFoundResponse(OutputStream output, String errorDocumentRoot) throws Exception {
        synchronized (System.out) {
            System.out.println("Not Found Response");
            System.out.flush();
        }
        Util.writeLine(output, "HTTP/1.1 404 Not Found");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Henacat/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-Type: text/html");
        Util.writeLine(output, "");
        try (var fis = new BufferedInputStream(new FileInputStream(errorDocumentRoot + "/404.html"));) {
            int ch;
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
        }
    }

    public static void sendMovePermanentlyResponse(OutputStream output, String location) throws Exception {
        Util.writeLine(output, "HTTP/1.1 301 Moved Permanently");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Henacat/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Location: " + location);
        Util.writeLine(output, "");
    }

    public static void sendFoundResponse(OutputStream output, String location) throws Exception {
        Util.writeLine(output, "HTTP/1.1 302 Found");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Henacat/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Location: " + location);
        Util.writeLine(output, "");
    }

    public static void sendOkResponse(OutputStream output, InputStream fis, String ext) throws Exception {
        synchronized (System.out) {
            System.out.println("Ok");
            System.out.flush();
        }
        Util.writeLine(output, "HTTP/1.1 200 OK");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Henacat/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-Type: " + Util.getContentType(ext));
        Util.writeLine(output, "");

        int ch;
        while ((ch = fis.read()) != -1) {
            output.write(ch);
        }
    }

    public static void sendOkResponseHeader(OutputStream output, String contentType, ResponseHeaderGenerator hg)
            throws IOException {
        Util.writeLine(output, "HTTP/1.1 200 OK");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Henacat/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-Type: " + contentType);
        hg.generate(output);
        Util.writeLine(output, "");
    }
}
