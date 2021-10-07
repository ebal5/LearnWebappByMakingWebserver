package com.example.henacat.servletimpl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.henacat.servlet.http.HttpSession;

public class HttpSessionImpl implements HttpSession {
    private String id;
    private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
    private volatile long lastAccessedTime;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        var names = new HashSet<String>();
        names.addAll(attributes.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (value == null) {
            removeAttribute(name);
            return;
        }
        this.attributes.put(name, value);
    }

    public synchronized void access() {
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    public HttpSessionImpl(String id) {
        this.id = id;
        this.access();
    }
}
