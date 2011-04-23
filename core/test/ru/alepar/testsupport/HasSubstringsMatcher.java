package ru.alepar.testsupport;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class HasSubstringsMatcher extends BaseMatcher<String> {

    private final String[] substrings;

    public HasSubstringsMatcher(String... substrings) {
        this.substrings = substrings;
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

        for (String substring : substrings) {
            if (!str.contains(substring)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("string with following substrings ");
        for (String substring : substrings) {
            description.appendValue(substring);
        }
    }
}
