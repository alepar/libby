package ru.alepar.lib.translit;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SomeTranslitTest {

    private final ToLatTranslit translit = new SomeTranslit();

    @Test
    public void rusWordsAreProperlyTransliterated() throws Exception {
        assertThat(translit.lat("привет"), equalTo("privet"));
    }

    @Test
    public void nonRusCharsAreLeftAsIs() throws Exception {
        assertThat(translit.lat("hello мир"), equalTo("hello mir"));
    }

    @Test
    public void specialCharsAreOkToo() throws Exception {
        assertThat(translit.lat("!@#$ мир"), equalTo("!@#$ mir"));
    }


}
