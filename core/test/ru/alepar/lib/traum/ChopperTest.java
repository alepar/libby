package ru.alepar.lib.traum;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ChopperTest {

    @Test
    public void worksfineWithRootAsABaseDir() throws Exception {
        Chopper chopper = new Chopper(new File("\\"));

        assertThat(chopper.chop(new File("\\ru\\alepar\\smth")), equalTo("ru\\alepar\\smth"));
    }

    @Test
    public void worksfineWithSubfolderAsABaseDir() throws Exception {
        Chopper chopper = new Chopper(new File("\\ru"));

        assertThat(chopper.chop(new File("\\ru\\alepar\\smth")), equalTo("alepar\\smth"));
    }

    @Test
    public void someOtherWeirdCaseIsFineToo() throws Exception {
        Chopper chopper = new Chopper(new File("\\test"));

        assertThat(chopper.chop(new File("\\test\\ru\\Г\\Громов Борис\\Громов - Лубянская ласточка.fb2.zip")), equalTo("ru\\Г\\Громов Борис\\Громов - Лубянская ласточка.fb2.zip"));
    }
}
