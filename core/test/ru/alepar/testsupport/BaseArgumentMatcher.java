package ru.alepar.testsupport;

import org.hamcrest.BaseMatcher;
import ru.alepar.lib.stuff.Oops;

public abstract class BaseArgumentMatcher extends BaseMatcher<String> {
    private static final int IN_THE_MIDDLE_OF_THE_ARGUMENT = 0;
    private static final int IN_BETWEEN = 1;
    private static final int WAIT_FOR_QUOTES = 2;
    protected final int argNum;

    public BaseArgumentMatcher(int argNum) {
        this.argNum = argNum;
    }

    protected String extractArg(String str) {
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
        String arg;
        if (start == -1) {
            arg = null;
        }
        if (end == -1) {
            end = str.length();
        }
        arg = str.substring(start, end);
        return arg;
    }
}
