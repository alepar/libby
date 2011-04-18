package ru.alepar.testsupport;

import org.hamcrest.Matcher;

public class MyMatchers {

    public static <T> Matcher<T[]> item(int item, Matcher<T> itemMatcher) {
        return new ArrayItemMatcher<T>(item, itemMatcher);
    }

    public static Matcher<String> like(String regexp) {
        return new StringRegexpMatcher(regexp);
    }

}
