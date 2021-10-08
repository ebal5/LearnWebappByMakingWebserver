package com.example.henacat.servletimpl;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.example.henacat.servlet.http.Part;

public class PartImpl implements Part {
    private String contentType;
    private Map<String, String> headerMap;
    private byte[] data;
    private String name;

    public PartImpl(String contentType, HashMap<String, String> headerMap, byte[] data, String name) {
        this.contentType = contentType;
        this.headerMap = headerMap;
        this.data = data;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public void write(String fileName) throws IOException {
        try (var fos = new FileOutputStream(fileName)) {
            fos.write(data);
        }
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getHeader(String name) {
        return headerMap.get(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headerMap.keySet();
    }

    @Override
    public long getSize() {
        return data.length;
    }

}
