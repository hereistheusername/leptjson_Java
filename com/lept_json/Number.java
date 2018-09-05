package com.lept_json;

public class Number extends JsonLept{
    private double value = 0;

    Number() {}

    Number(double value) {
        this.value = value;
    }

    void setValue(double value) {
        this.value = value;
    }

    double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
