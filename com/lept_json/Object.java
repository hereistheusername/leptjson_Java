package com.lept_json;

import java.util.LinkedHashMap;

public class Object extends JsonLept{

    private LinkedHashMap<String, ? super JsonLept> object = new LinkedHashMap<>();

    public void addElement(String k, JsonLept v) {object.put(k, v);}

    @Override
    public String toString() {
        return object.toString();
    }
}
