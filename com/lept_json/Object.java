package com.lept_json;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Object extends JsonLept{

    private LinkedHashMap<String, ? super JsonLept> object = new LinkedHashMap<>();

    public void addElement(String k, JsonLept v) {object.put(k, v);}

    @Override
    public String toString() {
        Iterator<? extends Map.Entry<String, ? super JsonLept>> i = object.entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Map.Entry<String, ? super JsonLept> e = i.next();
            String key = e.getKey();
            JsonLept value = (JsonLept) e.getValue();
            sb.append('"').append(key).append('"');
            sb.append(": ");
            sb.append(value);
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }
}
