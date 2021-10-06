package com.example.henacat.webserver;

import java.net.ServerSocket;

import com.example.henacat.servletimpl.WebApplication;

public class Main {
    public static void main(String[] args) throws Exception {
        var app = WebApplication.createInstance("testbbs");
        app.addServlet("/ShowBBS", "ShowBBS");
        app.addServlet("/PostBBS", "PostBBS");
        try (var server = new ServerSocket(8001)) {
            for (;;) {
                var socket = server.accept();
                var serverThread = new ServerThread(socket);
                var thread = new Thread(serverThread);
                thread.start();
            }
        }
    }
}
