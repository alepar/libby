package ru.alepar.testsupport;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static java.util.regex.Pattern.quote;

public class HasWordsMatcher extends BaseMatcher<String> {

    private final String[] substrings;

    public HasWordsMatcher(String... substrings) {
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
            if (!str.matches(".*(^|[^a-zA-Zа-яА-Я0-9])" + quote(substring) + "([^a-zA-Zа-яА-Я0-9]|$).*")) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("string with following substrings");
        for (String substring : substrings) {
            description.appendText(" ");
            description.appendValue(substring);
        }
    }
}
