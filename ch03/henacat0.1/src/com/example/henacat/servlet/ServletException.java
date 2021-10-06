package com.example.henacat.servlet;

public class ServletException extends Exception {
    public ServletException(String mes) {
        super(mes);
    }

    public ServletException(String mes, Throwable cause) {
        super(mes, cause);
    }

    // 書籍はjava.langThrowableだけど区別する意味分かんないので
    public ServletException(Throwable cause) {
        super(cause);
    }
}
