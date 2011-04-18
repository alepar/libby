package ru.alepar.testsupport;

import org.hamcrest.Description;

public class ArgumentRegexpMatcher extends BaseArgumentMatcher {

    private final String argumentPattern;

    public ArgumentRegexpMatcher(int argNum, String argumentPattern) {
        super(argNum);
        this.argumentPattern = argumentPattern;
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof String)) {
            return false;
        }
        String str = (String) o;

        String arg = extractArg(str);
        return arg != null && arg.matches(argumentPattern);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("cmd where argument no ")
                .appendValue(argNum)
                .appendText(" matches ")
                .appendValue(argumentPattern);
    }
}
