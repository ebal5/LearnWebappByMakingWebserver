package com.example.henacat.servlet.http;

import java.io.IOException;

import com.example.henacat.servlet.ServletException;

public class HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("GET")) {
            doGet(req, resp);
        } else if (req.getMethod().equals("POST")) {
            doPost(req, resp);
        } else {
            throw new ServletException("Not Implemented Method: " + req.getMethod());
        }
    }
}
