package ru.alepar.testsupport;

import org.hamcrest.Description;

public class ArgumentMatcher extends BaseArgumentMatcher {

    private final String argument;

    public ArgumentMatcher(int argNum, String argument) {
        super(argNum);
        this.argument = argument;
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof String)) {
            return false;
        }
        String str = (String) o;

        String arg = extractArg(str);
        return arg != null && arg.equals(argument);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("cmd where argument no ")
                .appendValue(argNum)
                .appendText(" is ")
                .appendValue(argument);
    }
}
