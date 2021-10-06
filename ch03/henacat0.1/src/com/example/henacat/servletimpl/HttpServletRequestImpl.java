package com.example.henacat.servletimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import com.example.henacat.util.MyURLDecoder;

public class HttpServletRequestImpl implements HttpServletRequest {
    private String method;
    private String characterEncoding = "ISO-8859-1";
    private Map<String, String[]> parameterMap;

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

    public HttpServletRequestImpl(String method, Map<String, String[]> parameterMap) {
        this.method = method;
        this.parameterMap = parameterMap;
    }
}
