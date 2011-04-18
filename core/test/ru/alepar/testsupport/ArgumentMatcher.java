package ru.alepar.testsupport;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import ru.alepar.lib.stuff.Oops;

public class ArgumentMatcher extends BaseMatcher<String> {

    private static final int IN_THE_MIDDLE_OF_THE_ARGUMENT = 0;
    private static final int IN_BETWEEN = 1;
    private static final int WAIT_FOR_QUOTES = 2;

    private final String argument;
    private final int argNum;

    public ArgumentMatcher(int argNum, String argument) {
        this.argNum = argNum;
        this.argument = argument;
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof String)) {
            return false;
        }
        String str = (String) o;

        int start = -1, end = -1;

        int argCount = 0;
        int state = IN_THE_MIDDLE_OF_THE_ARGUMENT;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (state) {
                case IN_THE_MIDDLE_OF_THE_ARGUMENT:
                    if (c == ' ') {
                        state = IN_BETWEEN;
                        if (argCount == argNum) {
                            end = i;
                        }
                    }
                    break;
                case IN_BETWEEN:
                    if (c != ' ') {
                        argCount++;
                        if (c == '"') {
                            state = WAIT_FOR_QUOTES;
                            i++;
                        } else {
                            state = IN_THE_MIDDLE_OF_THE_ARGUMENT;
                        }
                        if (argCount == argNum) {
                            start = i;
                        }
                    }
                    break;
                case WAIT_FOR_QUOTES:
                    if (c == '"') {
                        state = IN_BETWEEN;
                        if (argCount == argNum) {
                            end = i;
                        }
                    }
                    break;
                default:
                    throw new Oops("" + state);
            }
        }
        if (end == -1) {
            end = str.length();
        }
        return str.substring(start, end).equals(argument);
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
