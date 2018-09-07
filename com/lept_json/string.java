package com.lept_json;

public class string extends JsonLept{

    private String value = null;

    public string() {}

    public string(String value) {this.value = value;}

    @Override
    public String toString() {
        return new StringBuilder().append('"').append(value).append('"').toString();
    }
}
