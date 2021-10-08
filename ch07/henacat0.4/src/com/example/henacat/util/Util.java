package com.example.henacat.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class Util {
    public static String readLine(InputStream input) throws IOException {
        int ch;
        String ret = "";
        while ((ch = input.read()) != -1) {
            if (ch == '\r') {

            } else if (ch == '\n') {
                break;
            } else {
                ret += (char) ch;
            }
        }
        if (ch == -1) {
            return null;
        } else {
            return ret;
        }
    }

    public static void writeLine(OutputStream output, String str) throws IOException {
        for (char ch : str.toCharArray()) {
            output.write((int) ch);
        }
        output.write((int) '\r');
        output.write((int) '\n');
    }

    public static String getDateStringUtc() {
        var cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        var df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + " GMT";
    }

    private static final HashMap<String, String> contentTypeMap = new HashMap<String, String>() {
        {
            put("html", "text/html");
            put("htm", "text/html");
            put("txt", "text/plain");
            put("md", "text/plain");
            put("css", "text/css");
            put("png", "image/png");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("gif", "image/gif");
        }
    };

    public static String getContentType(String ext) {
        var ret = contentTypeMap.getOrDefault(ext, "");
        if (ret == "") {
            return "application/octet-stream";
        } else {
            return ret;
        }
    }

    public static ContentType parseContentType(String str) {
        var temp = str.split(" *; *");
        var typeSubType = temp[0].split("/");
        var type = typeSubType[0];
        String subType = null;
        if (typeSubType.length > 1) {
            subType = typeSubType[1];
        }
        var attributes = new HashMap<String, String>();

        for (var i = 1; i < temp.length; i++) {
            var kv = temp[i].split("=");
            attributes.put(kv[0].toUpperCase(), kv[1]);
        }
        return new ContentType(type, subType, attributes);
    }

    public static void parseHeader(HashMap<String, String> headerMap, String line) {
        var colonPos = line.indexOf(":");
        if (colonPos == -1)
            return;

        var headerName = line.substring(0, colonPos).toUpperCase();
        var headerValue = line.substring(colonPos + 1).trim();
        headerMap.put(headerName, headerValue);
    }
}