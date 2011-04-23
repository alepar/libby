package ru.alepar.lib.list;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Folder;
import ru.alepar.lib.model.Item;
import ru.alepar.lib.traum.ItemStorage;
import ru.alepar.testsupport.MockFileSystem;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static ru.alepar.lib.traum.PathUtils.createPath;

@RunWith(JMock.class)
public class TraumListerTest {

    private final Mockery mockery = new JUnit4Mockery();

    private final File root = new File(".");
    private final ItemStorage storage = mockery.mock(ItemStorage.class);
    private final MockFileSystem fileSystem = new MockFileSystem();
    private final TraumLister lister = new TraumLister(storage, fileSystem);

    @Test
    public void listsFolderWithAuthorsWithoutExceptions() {
        String nameOne = "Computers";
        String nameTwo = "Crash Malice";

        final String basePath = createPath("ru", "C");
        final String pathOne = createPath(basePath, nameOne);
        final String pathTwo = createPath(basePath, nameTwo);
        final Folder folderBase = new Folder(basePath, "C");
        final Author authorOne = new Author(pathOne, nameOne);
        final Author authorTwo = new Author(pathTwo, nameTwo);

        fileSystem.add(basePath, true);
        fileSystem.add(pathOne, true);
        fileSystem.add(pathTwo, true);

        mockery.checking(new Expectations() {{
            allowing(storage).get(basePath);
            will(returnValue(folderBase));

            allowing(storage).get(pathOne);
            will(returnValue(authorOne));

            allowing(storage).get(pathTwo);
            will(returnValue(authorTwo));
        }});

        List<Item> items = lister.list(basePath);

        assertThat(items, equalTo(Arrays.<Item>asList(
                authorOne,
                authorTwo
        )));
    }

}
