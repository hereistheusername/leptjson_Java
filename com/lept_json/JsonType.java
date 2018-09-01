package com.lept_json;

public abstract class JsonType {

    public String getType() {
        return this.getClass().getSimpleName();
    }

}
