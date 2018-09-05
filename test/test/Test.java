package test;

import com.lept_json.JsonLept;
import com.lept_json.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    static int count = 0;
    static int passCount = 0;

    static Thread currentThread = Thread.currentThread();

    static void println(String s) {
        System.out.println(s);
    }

    static void expectEqBase(boolean equal, String expectation, String actuality) {
        count++;
        if (equal) {
            passCount++;
        } else {
            System.err.format("%s:%d: expect: %s actual: %s\n",
                    currentThread.getStackTrace()[1].getFileName(),
                    currentThread.getStackTrace()[1].getLineNumber(),
                    expectation,
                    actuality);
        }
    }

    static void expectEqBase(boolean equal, double expectation, String actuality) {
        count++;
        if (equal) {
            passCount++;
        } else {
            System.err.format("%s:%d: expect: %f actual: %s\n",
                    currentThread.getStackTrace()[1].getFileName(),
                    currentThread.getStackTrace()[1].getLineNumber(),
                    expectation,
                    actuality);
        }
    }

    static void expectEqString(String expectation, String actuality) { expectEqBase(expectation.equals(actuality), expectation, actuality); }
    static void expectEqNumber(double expectation, JsonLept actuality) {
        String value = actuality.toString();
        expectEqBase(expectation == Double.parseDouble(value), expectation, value);
    }
    static void parseLiteral() {
        Parser parser = new Parser();
        expectEqString("true", parser.parse("  true").toString());
        expectEqString("false", parser.parse("false  ").toString());
        expectEqString("null", parser.parse(" null  ").toString());
        try {
            expectEqString("null", parser.parse("nUll  ").toString());
            passCount++;
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Invalid Value")) {passCount++;}
        }finally {
            count++;
        }
    }

    static void parseNumber() {
        Parser parser = new Parser();
        expectEqNumber(23e3, parser.parse("23e3"));
        expectEqNumber(0.0, parser.parse("0"));
        expectEqNumber(0.0, parser.parse("-0"));
        expectEqNumber(0.0, parser.parse("-0.0"));
        expectEqNumber(1.0, parser.parse("1"));
        expectEqNumber(-1.0, parser.parse("-1"));
        expectEqNumber(1.5, parser.parse("1.5"));
        expectEqNumber(-1.5, parser.parse("-1.5"));
        expectEqNumber(3.1416, parser.parse("3.1416"));
        expectEqNumber(1E10, parser.parse("1E10"));
        expectEqNumber(1e10, parser.parse("1e10"));
        expectEqNumber(1E+10, parser.parse("1E+10"));
        expectEqNumber(1E-10, parser.parse("1E-10"));
        expectEqNumber(-1E10, parser.parse("-1E10"));
        expectEqNumber(-1e10, parser.parse("-1e10"));
        expectEqNumber(-1E+10, parser.parse("-1E+10"));
        expectEqNumber(-1E-10, parser.parse("-1E-10"));
        expectEqNumber(1.234E+10, parser.parse("1.234E+10"));
        expectEqNumber(1.234E-10, parser.parse("1.234E-10"));
        expectEqNumber(0.0, parser.parse("1e-10000")); /* must underflow */

        expectEqNumber(1.0000000000000002, parser.parse("1.0000000000000002")); /* the smallest number > 1 */
        expectEqNumber( 4.9406564584124654e-324, parser.parse("4.9406564584124654e-324")); /* minimum denormal */
        expectEqNumber(-4.9406564584124654e-324, parser.parse("-4.9406564584124654e-324"));
        expectEqNumber( 2.2250738585072009e-308, parser.parse("2.2250738585072009e-308"));  /* Max subnormal double */
        expectEqNumber(-2.2250738585072009e-308, parser.parse("-2.2250738585072009e-308"));
        expectEqNumber( 2.2250738585072014e-308, parser.parse("2.2250738585072014e-308"));  /* Min normal positive double */
        expectEqNumber(-2.2250738585072014e-308, parser.parse("-2.2250738585072014e-308"));
        expectEqNumber( 1.7976931348623157e+308, parser.parse("1.7976931348623157e+308"));  /* Max double */
        expectEqNumber(-1.7976931348623157e+308, parser.parse("-1.7976931348623157e+308"));
    }

    static void parseString() {
        Parser parser = new Parser();
        expectEqString("helloworld", parser.parse("\"helloworld\"").toString());
        try {
            expectEqString("helloworld", parser.parse("\"hellowolrd").toString());
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Invalid String Value")) {passCount++;}
        } finally {
            count++;
        }
    }

    static void parseArray() {
        Parser parser = new Parser();
        expectEqString("[1.0, 2.0, 3.0, true, false, null, [1.0, 2.0, 233.0]]", parser.parse("[1, 2, 3, true, false, null, [1, 2, 233]]").toString());
    }
    static void indexTest() {
        String s = "HelloWorld";
        int i;
        for (i = 0; i < s.length(); i++) {
            if (i % 2 == 0) {continue;}
            System.out.println(s.charAt(i));
        }
        System.out.println(i == s.length());
    }

    public static void main(String[] args) {
//        indexTest();
        parseLiteral();
        parseNumber();
        parseString();
        parseArray();
        System.out.format("%d/%d (%3.2f%%) passed\n", passCount, count, passCount * 100.0 / count);
    }
}
