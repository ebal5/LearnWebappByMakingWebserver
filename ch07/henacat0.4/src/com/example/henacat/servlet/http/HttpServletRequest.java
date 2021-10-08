package com.example.henacat.servlet.http;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

public interface HttpServletRequest {
    String getMethod();

    String getParameter(String name);

    String[] getParameterValues(String name);

    void setCharacterEncoding(String env) throws UnsupportedEncodingException;

    Cookie[] getCookies();

    HttpSession getSession();

    HttpSession getSession(boolean create);

    Part getPart(String name);

    Collection<Part> getParts();
}
