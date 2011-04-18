package ru.alepar.testsupport;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ArgumentMatcherTest {

    @Test
    public void getsFirstArgRight() throws Exception {
        ArgumentMatcher matcher = new ArgumentMatcher(1, "arg_1");

        assertThat(matcher.matches("c:\\smth arg_1"), equalTo(true));
        assertThat(matcher.matches("c:\\smth agg_1"), equalTo(false));
        assertThat(matcher.matches("/smth/smth.sh arg_1"), equalTo(true));

        assertThat(matcher.matches("/smth/smth.sh arg_1 arg_2"), equalTo(true));
        assertThat(matcher.matches("/smth/smth.sh arg_2 arg_1"), equalTo(false));
    }

    @Test
    public void ignoresSpacesWhileInQuotes() throws Exception {
        ArgumentMatcher matcher = new ArgumentMatcher(2, "arg_2");

        assertThat(matcher.matches("c:\\smth \"no matter how     many soaces    here\" arg_2"), equalTo(true));
    }

    @Test
    public void matchesArgInQuotesFine() throws Exception {
        ArgumentMatcher matcher = new ArgumentMatcher(1, "no matter how     many soaces    here");

        assertThat(matcher.matches("c:\\smth \"no matter how     many soaces    here\" arg_2"), equalTo(true));
    }
}
