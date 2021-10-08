package com.example.henacat.util;

import java.util.Map;

public class ContentType {
    private String type;
    private String subType;
    private Map<String, String> attributes;

    public String getType() {
        return this.type;
    }

    public String getSubType() {
        return this.subType;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    ContentType(String type, String subType, Map<String, String> attributes) {
        this.type = type;
        this.subType = subType;
        this.attributes = attributes;
    }
}
