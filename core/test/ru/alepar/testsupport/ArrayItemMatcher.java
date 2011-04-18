package ru.alepar.testsupport;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ArrayItemMatcher<T> extends BaseMatcher<T[]> {

    private final int item;
    private final Matcher<T> itemMatcher;

    public ArrayItemMatcher(int item, Matcher<T> itemMatcher) {
        this.item = item;
        this.itemMatcher = itemMatcher;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean matches(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Object[])) {
            return false;
        }

        T[] arr = (T[]) o;
        return itemMatcher.matches(arr[item]);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("array where item ")
                .appendValue(item)
                .appendText(" ");
        itemMatcher.describeTo(description);
    }
}
