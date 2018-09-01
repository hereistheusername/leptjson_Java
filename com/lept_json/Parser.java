package com.lept_json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public JsonType jsonType;

    private static final String[] types = {"null", "true", "false"};

    private Pattern[] patterns = new Pattern[types.length];

    public Parser() {
        for (int i = 0; i < types.length; i++) {
            patterns[i] = Pattern.compile(types[i]);
        }
    }

    public void parse(String s) {
        char[] ch = s.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            switch (ch[i]) {
                case 'n': parseLiteral(s.substring(i, i + 4), 0); i += 4; break;
                case 't': parseLiteral(s.substring(i, i + 4), 1); i += 4; break;
                case 'f': parseLiteral(s.substring(i, i + 5), 2); i += 5; break;
            }
        }
    }

    void parseLiteral(String s, int index) {
        if (!s.equals(types[index])) {throw new RuntimeException("Invalid Value");}
        switch (index) {
            case 0: jsonType = new NULL(); return;
            case 1: jsonType = new Boolean(true); return;
            case 2: jsonType = new Boolean(false); return;
        }
    }
}
