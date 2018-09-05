package com.lept_json;

import java.util.LinkedList;

public class Array extends JsonLept{

    private LinkedList<JsonLept> array = new LinkedList<>();

    public void addElement(JsonLept j) {
        array.add(j);
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
