package ru.alepar.lib.translit;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AleparTranslitTest {

    private final Translit translit = new AleparTranslit();

    @Test
    public void branchesProperlyOnEncouteringDoubleMeaningSymbols() throws Exception {
        assertThat(translit.translate("ja"), hasItems("йа", "я"));
    }

    @Test
    public void replacesSingleQuoteProperly() throws Exception {
        assertThat(translit.translate("abv'"), hasItems("абвь", "абвъ"));
    }
}
