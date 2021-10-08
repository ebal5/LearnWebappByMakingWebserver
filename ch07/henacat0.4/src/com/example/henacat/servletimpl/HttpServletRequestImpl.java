package com.example.henacat.servletimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.example.henacat.servlet.http.Cookie;
import com.example.henacat.servlet.http.HttpServletRequest;
import com.example.henacat.servlet.http.HttpSession;
import com.example.henacat.servlet.http.Part;
import com.example.henacat.util.MyURLDecoder;

public class HttpServletRequestImpl implements HttpServletRequest {
    private String method;
    private String characterEncoding = "ISO-8859-1";
    // multipart/form-data なら byteParameterMapを使うので
    private Map<String, String[]> parameterMap;
    private Map<String, byte[][]> byteParameterMap;
    private Cookie[] cookies;
    private HttpSessionImpl session;
    private ArrayList<Part> partList;
    private HttpServletResponseImpl response;
    private WebApplication webApp;
    private final String SESSION_COOKIE_ID = "JSESSIONID";

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
        String[] decoded;
        if (this.parameterMap != null) {
            var values = this.parameterMap.get(name);
            if (values == null) {
                return null;
            }
            decoded = new String[values.length];
            try {
                for (var i = 0; i < values.length; i++) {
                    decoded[i] = MyURLDecoder.decode(values[i], this.characterEncoding);
                }
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        } else {
            var data = this.byteParameterMap.get(name);
            if (data == null) {
                return null;
            }
            decoded = new String[data.length];
            try {
                for (var i = 0; i < data.length; i++) {
                    decoded[i] = new String(data[0], this.characterEncoding);
                }
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
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

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (!create) {
            return this.session;
        }
        if (this.session == null) {
            var manager = this.webApp.getSessionManager();
            this.session = manager.createSession();
            addSessionCookie();
        }
        return this.session;
    }

    private HttpSessionImpl getSessionInternal() {
        if (this.cookies == null) {
            return null;
        }
        Cookie cookie = null;
        for (var tempCookie : this.cookies) {
            if (tempCookie.getName().equals(SESSION_COOKIE_ID)) {
                cookie = tempCookie;
            }
        }
        var manager = this.webApp.getSessionManager();
        HttpSessionImpl ret = null;
        if (cookie != null) {
            ret = manager.getSession(cookie.getValue());
        }
        return ret;
    }

    private void addSessionCookie() {
        var cookie = new Cookie(SESSION_COOKIE_ID, this.session.getId());
        cookie.setPath("/" + webApp.directory);
        cookie.setHttpOnly(true);
        this.response.addCookie(cookie);
    }

    @Override
    public Part getPart(String name) {
        for (var part : this.partList) {
            if (part.getName().equals(name)) {
                return part;
            }
        }
        return null;
    }

    @Override
    public Collection<Part> getParts() {
        return this.partList;
    }

    private HttpServletRequestImpl(String method, Map<String, String> requestHeader, HttpServletResponseImpl resp,
            WebApplication webApp) {
        this.method = method;
        this.cookies = parseCookies(requestHeader.get("COOKIE"));
        this.response = resp;
        this.webApp = webApp;
        this.session = getSessionInternal();
        if (this.session != null) {
            addSessionCookie();
        }
    }

    public HttpServletRequestImpl(String method, Map<String, String> requestHeader, Map<String, String[]> parameterMap,
            HttpServletResponseImpl resp, WebApplication webApp) {
        this(method, requestHeader, resp, webApp);
        this.parameterMap = parameterMap;
    }

    public HttpServletRequestImpl(Map<String, String> requestHeader, HashMap<String, byte[][]> byteParameterMap,
            ArrayList<Part> partList, HttpServletResponseImpl resp, WebApplication webApp) {
        this("POST", requestHeader, resp, webApp);
        this.partList = partList;
        this.byteParameterMap = byteParameterMap;
    }

}
