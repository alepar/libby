package ru.alepar.lib.traum;

import org.junit.Test;
import ru.alepar.lib.fs.Chopper;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ChopperTest {

    @Test
    public void worksfineWithRootAsABaseDir() throws Exception {
        Chopper chopper = new Chopper(new File(concat("", "")));

        assertThat(chopper.chop(new File(concat("", "ru", "alepar", "smth"))), equalTo(concat("ru", "alepar", "smth")));
    }

    @Test
    public void worksfineWithSubfolderAsABaseDir() throws Exception {
        Chopper chopper = new Chopper(new File("", "ru"));

        assertThat(chopper.chop(new File(concat("", "ru", "alepar", "smth"))), equalTo(concat("alepar", "smth")));
    }

    @Test
    public void someOtherWeirdCaseIsFineToo() throws Exception {
        Chopper chopper = new Chopper(new File("", "test"));

        assertThat(chopper.chop(new File(concat("", "test", "ru", "Г", "Громов Борис", "Громов - Лубянская ласточка.fb2.zip"))), equalTo(concat("ru", "Г", "Громов Борис", "Громов - Лубянская ласточка.fb2.zip")));
    }

    @Test
    public void chopsBasePathToDot() throws Exception {
        Chopper chopper = new Chopper(new File("", "test"));

        assertThat(chopper.chop(new File("", "test")), equalTo("."));
    }

    private static String concat(String... names) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            if (i > 0) {
                result.append(File.separatorChar);
            }
            result.append(name);
        }
        return result.toString();
    }

}
