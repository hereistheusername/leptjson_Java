package com.lept_json;

public class Boolean extends JsonLept {

    public boolean value = false;

    public Boolean() {}

    public Boolean(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value? "true": "false";
    }
}
