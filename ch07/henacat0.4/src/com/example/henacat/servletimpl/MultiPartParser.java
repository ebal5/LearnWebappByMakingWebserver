package com.example.henacat.servletimpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.henacat.servlet.http.Part;
import com.example.henacat.util.Util;

public class MultiPartParser {
    static HttpServletRequestImpl parse(Map<String, String> reqHeader, InputStream input, String contentTypeStr,
            int contentLength, HttpServletResponseImpl resp, WebApplication webApp) throws IOException {
        var contentType = Util.parseContentType(contentTypeStr);
        if (contentType == null) {
            synchronized (System.out) {
                System.out.println("NullType: " + contentTypeStr);
            }
        }
        var boundary = "--" + contentType.getAttribute("BOUNDARY");
        var partList = new ArrayList<Part>();
        var length = contentLength;
        length = readToBoundary(input, boundary, length, null);
        var parameterMap = new HashMap<String, byte[][]>();
        for (;;) {
            var headerMap = new HashMap<String, String>();
            String line;
            while ((line = Util.readLine(input)) != null) {
                length -= line.length() + 2; /* 0x0D, 0x0A の分 */
                if (line == "") {
                    break;
                }
                Util.parseHeader(headerMap, line);
            }
            var cd = Util.parseContentType(headerMap.get("CONTENT-DISPOSITION"));
            var quotedName = cd.getAttribute("NAME");
            var name = quotedName.substring(1, quotedName.length() - 1);
            var ct = headerMap.get("CONTENT_TYPE");
            var dataOut = new byte[1][];
            length = readToBoundary(input, "\r\n" + boundary, length, dataOut);
            var part = new PartImpl(ct, headerMap, dataOut[0], name);
            partList.add(part);
            if (ct == null) {
                var array = parameterMap.get(name);
                if (array == null) {
                    parameterMap.put(name, new byte[][] { dataOut[0] });
                } else {
                    var newArray = new byte[array.length + 1][];
                    System.arraycopy(array, 0, newArray, 0, array.length);
                    newArray[array.length] = dataOut[0];
                    parameterMap.put(name, newArray);
                }
            }
            if (length == 0) {
                break;
            }
        }
        var req = new HttpServletRequestImpl(reqHeader, parameterMap, partList, resp, webApp);
        return req;
    }

    private static int readToBoundary(InputStream input, String boundary, int length, byte[][] dataOut)
            throws IOException {
        var out = new ByteArrayOutputStream();
        int ch;
        var bPos = 0;
        var found = false;
        while ((ch = input.read()) != -1 && length > 0) {
            length--;
            if (ch == boundary.charAt(bPos)) {
                bPos++;
                if (bPos == boundary.length()) {
                    found = true;
                    ch = input.read();
                    if (ch == '\r') {
                        input.read(); /* 0x0D, 0x0Aは読み飛ばすので */
                        length -= 2;
                    } else if (ch == '-') {
                        // --\r\n は読み飛ばす
                        input.read();
                        input.read();
                        input.read();
                        length -= 4;
                    }
                    break;
                }
            } else if (bPos > 0) {
                out.write(boundary.substring(0, bPos).getBytes("US-ASCII"));
                if (ch == boundary.charAt(0)) {
                    bPos = 1;
                } else {
                    bPos = 0;
                    out.write((byte) ch);
                }
            } else {
                out.write((byte) ch);
            }
        }
        if (found && dataOut != null) {
            dataOut[0] = out.toByteArray();
        }
        return length;
    }
}
