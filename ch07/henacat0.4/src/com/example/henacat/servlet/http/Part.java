package com.example.henacat.servlet.http;

import java.io.InputStream;
import java.util.Collection;

public interface Part {
    String getName();

    InputStream getInputStream();

    void write(String fileName);

    String getContentType();

    String getHeader(String name);

    Collection<String> getHeaderNames();

    long getSize();
}