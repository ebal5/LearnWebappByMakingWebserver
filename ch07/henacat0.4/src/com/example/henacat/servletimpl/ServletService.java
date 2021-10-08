package com.example.henacat.servletimpl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.henacat.servlet.http.HttpServlet;
import com.example.henacat.servlet.http.HttpServletRequest;
import com.example.henacat.servlet.http.HttpServletResponse;
import com.example.henacat.util.Constants;
import com.example.henacat.util.SendResponse;

public class ServletService {
    private static HttpServlet createServlet(ServletInfo info) throws Exception {
        Class<?> clazz = info.webApp.classLoader.loadClass(info.servletClassName);
        return (HttpServlet) clazz.newInstance();
    }

    private static Map<String, String[]> stringToMap(String str) {
        var parameterMap = new HashMap<String, String[]>();
        if (str == null)
            return parameterMap;

        var paramArray = str.split("&");
        for (var param : paramArray) {
            var keyValue = param.split("=");
            if (parameterMap.containsKey(keyValue[0])) {
                var array = parameterMap.get(keyValue[0]);
                var newArray = new String[array.length + 1];
                System.arraycopy(array, 0, newArray, 0, array.length);
                newArray[array.length] = keyValue[1];
                parameterMap.put(keyValue[0], newArray);
            } else {
                parameterMap.put(keyValue[0], new String[] { keyValue[1] });
            }
        }
        return parameterMap;
    }

    // Content-Lengthが指定されたとき、その長さ分コンテンツを読む
    private static String readToSize(InputStream input, int size) throws Exception {
        int ch;
        var sb = new StringBuilder();
        var readSize = 0;

        while (readSize < size && (ch = input.read()) != -1) {
            sb.append((char) ch);
            readSize++;
        }
        return sb.toString();
    }

    public static void doService(String method, String query, ServletInfo info, Map<String, String> reqHeader,
            InputStream input, OutputStream output) throws Exception {
        if (info.servlet == null) {
            info.servlet = createServlet(info);
        }

        var outputBuffer = new ByteArrayOutputStream();
        var resp = new HttpServletResponseImpl(outputBuffer);

        HttpServletRequest req;
        if (method.equals("GET")) {
            var map = stringToMap(query);
            req = new HttpServletRequestImpl("GET", reqHeader, map, resp, info.webApp);
        } else if (method.equals("POST")) {
            var contentType = reqHeader.get("CONTENT-TYPE");
            var contentLength = Integer.parseInt(reqHeader.get("CONTENT-LENGTH"));
            if (contentType.toUpperCase().startsWith("MULTIPART/FORM-DATA")) {
                req = MultiPartParser.parse(reqHeader, input, contentType, contentLength, resp, info.webApp);
            } else {
                var line = readToSize(input, contentLength);
                var map = stringToMap(line);
                req = new HttpServletRequestImpl("POST", reqHeader, map, resp, info.webApp);
            }
        } else {
            // GET POST 以外は非対応
            throw new AssertionError("BAD METHOD: " + method);
        }

        info.servlet.service(req, resp);

        if (resp.status == HttpServletResponse.SC_OK) {
            var hq = new ResponseHeaderGeneratorImpl(resp.cookies);
            SendResponse.sendOkResponseHeader(output, resp.contentType, hq);
            resp.printWriter.flush();
            var outputBytes = outputBuffer.toByteArray();
            for (var b : outputBytes) {
                output.write((int) b);
            }
        } else if (resp.status == HttpServletResponse.SC_FOUND) {
            String redirectLocation;
            if (resp.redirectLocation.startsWith("/")) {
                var host = reqHeader.get("HOST");
                redirectLocation = "http://" + ((host != null) ? host : Constants.SERVER_NAME) + resp.redirectLocation;
            } else {
                redirectLocation = resp.redirectLocation;
            }
            SendResponse.sendFoundResponse(output, redirectLocation);
        }
    }
}
