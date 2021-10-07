package com.example.henacat.servletimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import com.example.henacat.servlet.http.Cookie;
import com.example.henacat.servlet.http.HttpServletRequest;
import com.example.henacat.util.MyURLDecoder;

public class HttpServletRequestImpl implements HttpServletRequest {
    private String method;
    private String characterEncoding = "ISO-8859-1";
    private Map<String, String[]> parameterMap;
    private Cookie[] cookies;

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getParameter(String name) {
        var values = getParameterValues(name);
        if (values == null) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        var values = this.parameterMap.get(name);
        if (values == null) {
            return null;
        }
        var decoded = new String[values.length];
        try {
            for (var i = 0; i < values.length; i++) {
                decoded[i] = MyURLDecoder.decode(values[i], this.characterEncoding);
            }
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        return decoded;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        if (!Charset.isSupported(env)) {
            throw new UnsupportedEncodingException("encoding.." + env);
        }
        this.characterEncoding = env;
    }

    @Override
    public Cookie[] getCookies() {
        return this.cookies;
    }

    private static Cookie[] parseCookies(String cookieString) {
        if (cookieString == null) {
            return null;
        }
        var cookiePairArray = cookieString.split(";");
        var ret = new Cookie[cookiePairArray.length];
        var cookieCount = 0;

        for (var cookiePair : cookiePairArray) {
            var kv = cookiePair.split("=", 2);

            ret[cookieCount] = new Cookie(kv[0].trim(), kv[1]);
            cookieCount++;
        }

        return ret;
    }

    public HttpServletRequestImpl(String method, Map<String, String> requestHeader,
            Map<String, String[]> parameterMap) {
        this.method = method;
        this.parameterMap = parameterMap;
        this.cookies = parseCookies(requestHeader.get("COOKIE"));
    }
}
