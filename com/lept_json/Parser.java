package com.lept_json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public JsonLept jsonType;

    private static final String[] types = {"null", "true", "false", "([-\\d\\.eE]+?(?=[ |,|\\]|\\}])|[-\\d\\.eE]+?)",
            "(?<=\")[\\s\\S]+(?=\")", "(?<=\\[)[\\s\\S]+(?=\\])"};
    private static Pattern[] patterns = new Pattern[3];

    /**
     * 解析完数字后，在parseNumber()里修改这个值，让parse()的i越过已经解析的数字
     */
    private int jump = 0;

    static  {
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = Pattern.compile(types[i + 3]);
        }
    }

    public JsonLept parse(String s) {
        JsonLept ret = null;
        s = s.trim();
        int i = 0;
        switch (s.charAt(0)) {
            case 'n': ret = parseLiteral(s.substring(i, i + 4), 0); break;
            case 't': ret = parseLiteral(s.substring(i, i + 4), 1); break;
            case 'f': ret = parseLiteral(s.substring(i, i + 5), 2); break;
            case '"': ret = parseString(s.substring(i)); break;
            default : ret = parseNumber(s.substring(i)); break;
            case '[': ret = parseArray(s.substring(i)); break;
        }
        return ret;
    }

    private JsonLept parseArray(String s) {
        Array ret = new Array();
        Matcher m = patterns[2].matcher(s);
        if (!m.find()) {throw new RuntimeException("Missing Square Bracket");}
        s = s.substring(1, m.end());
        int i;
        for (i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case ' ': break;
                case ',': break;
                default :
                    ret.addElement(parse(s.substring(i)));
                    i += jump;
                    jump = 0;
                    break;
            }
        }
        jump = i + 1;
        return ret;
    }

    private JsonLept parseNumber(String s) {
        JsonLept ret = null;
        /**
         * java的字符串没有'\0'结束符，为了后面的操作不触发越界异常
         * 可以在字符串的末尾添加空字符'\0'
         * 进行数字解析的时候，既不会被误识别，也可以在parse()里裁剪掉
         */
        s += '\0';
        int i = 0;
        if (s.charAt(i) == '-') {i++;}
        if (s.charAt(i) == '0') {
            i++;
        } else {
            if (!isDigit1to9(s.charAt(i))) {throw new RuntimeException("Invalid Number");}
            for (i++; i < s.length() && isDigit(s.charAt(i)); i++);
        }
        if (s.charAt(i) == '.') {
            i++;
            if (!isDigit(s.charAt(i))) {throw new RuntimeException("Invalid Number");}
            for (i++; i < s.length() && isDigit(s.charAt(i)); i++);
        }
        if (s.charAt(i) == 'e' || s.charAt(i) == 'E') {
            i++;
            if (s.charAt(i) == '+' || s.charAt(i) == '-') {i++;}
            if (!isDigit(s.charAt(i))) {throw new RuntimeException("Invalid Value");}
            for (i++; i < s.length() && isDigit(s.charAt(i)); i++);
        }
        try {
            ret = new Number(Double.parseDouble(s.substring(0, i)));
            jump = i - 1;
        } catch (IllegalStateException e) {
            throw new RuntimeException("Invalid Value");
        }
        return ret;
    }

    private JsonLept parseString(String s) {
        JsonLept ret = null;
        Matcher m = patterns[1].matcher(s);
        if (!m.find()) {throw new RuntimeException("Invalid String Value");}
        ret = new string(s.substring(1, m.end()));
        jump = m.end() - 1;
        return ret;
    }

    private JsonLept parseLiteral(String s, int index) {
        JsonLept ret = null;
        if (!s.equals(types[index])) {throw new RuntimeException("Invalid Value");}
        switch (index) {
            case 0: ret = new NULL(); jump = 3; return ret;
            case 1: ret = new Boolean(true); jump = 3;return ret;
            case 2: ret = new Boolean(false); jump = 4;return ret;
            default: return ret;
        }
    }

    private JsonLept filter(String s) {
        JsonLept ret = null;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case ',':
                case ' ': break;
                default : ret = parse(s.substring(i)); break;
            }
        }
        return ret;
    }
    private static boolean isDigit1to9(char c) {return c >= '1' && c <= '9';}
    private static boolean isDigit(char c) {return c >= '0' && c <= '9';}
}
