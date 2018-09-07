package com.lept_json;

public class Parser {

    private String JSON = null;
    private int index = 0;

    private static final String[] types = {"null", "true", "false"};

    public JsonLept parse(String s) {
        if (s == null) {throw new RuntimeException("JSON cant be null");}
        JSON = s + '\0';
        index = 0;
        whiteSpace();
        return _parse_();
    }
    private JsonLept _parse_() {
        JsonLept ret = null;
        switch (JSON.charAt(index)) {
            case 'n': ret = parseLiteral(0); break;
            case 't': ret = parseLiteral(1); break;
            case 'f': ret = parseLiteral(2); break;
            case '"': ret = new string(parseStringAndKey()); break;
            default : ret = parseNumber(); break;
            case '[': ret = parseArray(); break;
            case '{': ret = parseObject(); break;
        }
        return ret;
    }

    private JsonLept parseObject() {
        Object ret = new Object();
        String k = null;
        JsonLept v = null;
        index++;
        if (JSON.charAt(index) == '}') {
            index++;
            return ret;
        }
        for (;;) {
            whiteSpace();
            if (JSON.charAt(index) == '"') {
                k = parseStringAndKey();
            } else {
                throw new RuntimeException("Missing Quotation Mark");
            }
            whiteSpace();
            if (JSON.charAt(index) == ':') {
                index++;
                whiteSpace();
            } else {
                throw new RuntimeException("Missing Colon");
            }
            ret.addElement(k, _parse_());
            whiteSpace();
            if (JSON.charAt(index) == ',') {
                index++;
                whiteSpace();
            } else if (JSON.charAt(index) == '}') {
                break;
            } else {
                throw new RuntimeException("Missing Comma or Curly Bracket");
            }
        }
        index++;
        return ret;
    }

    private String parseStringAndKey() {
        int pre_index = ++index;
        try {
            while (JSON.charAt(index) != '"') {index++;}
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Missing Quotation Mark");
        }
        index++;
        return JSON.substring(pre_index, index - 1);
    }

    private JsonLept parseArray() {
        Array ret = new Array();
        index++;
        if (JSON.charAt(index) == ']') {
            index++;
            return ret;
        }
        for (;;) {
            whiteSpace();
            ret.addElement(_parse_());
            whiteSpace();
            if (JSON.charAt(index) == ',') {
                index++;
                whiteSpace();
            } else if (JSON.charAt(index) == ']') {
                break;
            } else {
                throw new RuntimeException("Missing Comma or Square Bracket");
            }
        }
        index++;
        return ret;
    }

    private JsonLept parseNumber() {
        JsonLept ret = null;
        int pre_index = index;
        if (JSON.charAt(index) == '-') {index++;}
        if (JSON.charAt(index) == '0') {
            index++;
        } else {
            if (!isDigit1to9(JSON.charAt(index))) {throw new RuntimeException("Invalid Number");}
            for (index++; index < JSON.length() && isDigit(JSON.charAt(index)); index++);
        }
        if (JSON.charAt(index) == '.') {
            index++;
            if (!isDigit(JSON.charAt(index))) {throw new RuntimeException("Invalid Number");}
            for (index++; index < JSON.length() && isDigit(JSON.charAt(index)); index++);
        }
        if (JSON.charAt(index) == 'E' || JSON.charAt(index) == 'e') {
            index++;
            if (JSON.charAt(index) == '+' || JSON.charAt(index) == '-') {index++;}
            if (!isDigit(JSON.charAt(index))) {throw new RuntimeException("Invalid Value");}
            for (index++; index < JSON.length() && isDigit(JSON.charAt(index)); index++);
        }
        try {
            ret = new Number(Double.parseDouble(JSON.substring(pre_index, index)));
        } catch (IllegalStateException e ) {
            throw new RuntimeException("Invalid Value");
        }
        return ret;
    }

    private JsonLept parseLiteral(int type) {
        for (int i = 0; i < types[type].length(); i++, index++) {
            if (types[type].charAt(i) != JSON.charAt(index)) {throw new RuntimeException("Invalid Value");}
        }
        switch (type) {
            case 0: return new NULL();
            case 1: return new Boolean(true);
            case 2: return new Boolean(false);
            default: return null;
        }
    }

    private static boolean isDigit1to9(char c) {return c >= '1' && c <= '9';}
    private static boolean isDigit(char c) {return c >= '0' && c <= '9';}
    private void whiteSpace() {
        char c = JSON.charAt(index);
        while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
            index++;
            c = JSON.charAt(index);
        }
    }
}
