package com.example.henacat.servletimpl;

import com.example.henacat.servlet.http.HttpServlet;

public class ServletInfo {
    WebApplication webApp;
    String urlPattern;
    String servletClassName;
    HttpServlet servlet;

    public ServletInfo(WebApplication webApp, String urlPettern, String servletClassName) {
        this.webApp = webApp;
        this.urlPattern = urlPettern;
        this.servletClassName = servletClassName;
    }
}
