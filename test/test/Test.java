package test;

import com.lept_json.Parser;

import java.util.Arrays;

public class Test {
    static int count = 0;


    static void parseLiteral() {
        Parser parser = new Parser();
        parser.parse(" null");
        System.out.println(parser.jsonType);
        parser.parse("true  ");
        System.out.println(parser.jsonType);
        parser.parse("false");
        System.out.println(parser.jsonType);
    }

    public static void main(String[] args) {
        parseLiteral();
    }
}
