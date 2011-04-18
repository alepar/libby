package ru.alepar.testsupport;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class StringRegexpMatcher extends BaseMatcher<String> {
    private final String regexp;

    public StringRegexpMatcher(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean matches(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof String)) {
            return false;
        }

        String str = (String) o;
        return str.matches(regexp);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("string matching ").appendValue(regexp);
    }
}
