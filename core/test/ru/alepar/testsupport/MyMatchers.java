package ru.alepar.testsupport;

import org.hamcrest.Matcher;

public class MyMatchers {
    public static Matcher<String> arg(int argNum, String argument) {
        return new ArgumentMatcher(argNum, argument);
    }

    public static Matcher<String> argRegexp(int argNum, String inputPath) {
        return new ArgumentRegexpMatcher(argNum, inputPath);
    }
}
