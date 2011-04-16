package ru.alepar.lib.traum;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ChopperTest {

    @Test
    public void worksfineWithRootAsABaseDir() throws Exception {
        Chopper chopper = new Chopper(new File("c:\\"));

        assertThat(chopper.chop(new File("c:\\ru\\alepar\\smth")), equalTo("ru\\alepar\\smth"));
    }

    @Test
    public void worksfineWithSubfolderAsABaseDir() throws Exception {
        Chopper chopper = new Chopper(new File("c:\\ru"));

        assertThat(chopper.chop(new File("c:\\ru\\alepar\\smth")), equalTo("alepar\\smth"));
    }

}
