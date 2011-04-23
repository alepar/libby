package ru.alepar.lib.traum;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Book;
import ru.alepar.lib.model.Folder;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static ru.alepar.testsupport.MyMatchers.hasWords;

@RunWith(JMock.class)
public class FileSystemStorageTest {

    private final Mockery mockery = new JUnit4Mockery();

    private final FileSystem fs = mockery.mock(FileSystem.class);
    private final ItemStorage extractor = new FileSystemStorage(fs);

    @Test
    public void authorFolderPathReturnsAuthorObjectWithProperName() throws Exception {
        final String author = "Лукьяненко Сергей";
        final String path = PathUtils.createPath("ru", "Л", author);
        final File file = new File(path);

        mockery.checking(new Expectations() {{
            allowing(fs).createFile(path);
            will(returnValue(file));

            allowing(fs).exists(file);
            will(returnValue(true));

            allowing(fs).isDirectory(file);
            will(returnValue(true));

            allowing(fs).getName(file);
            will(returnValue(author));
        }});

        Author item = (Author) extractor.get(path);
        assertThat(item, equalTo(new Author(path, author)));
    }

    @Test
    public void shortFolderPathReturnsFolder() throws Exception {
        final String folderName = "Л";
        final String path = PathUtils.createPath("ru", folderName);
        final File file = new File(path);

        mockery.checking(new Expectations() {{
            allowing(fs).createFile(path);
            will(returnValue(file));

            allowing(fs).exists(file);
            will(returnValue(true));

            allowing(fs).isDirectory(file);
            will(returnValue(true));

            allowing(fs).getName(file);
            will(returnValue(folderName));
        }});

        Folder item = (Folder) extractor.get(path);
        assertThat(item, equalTo(new Folder(path, folderName)));
    }

    @Test
    public void underscoreFolderPathReturnsFolder() throws Exception {
        final String folderName = "_scifi";
        final String path = PathUtils.createPath("ru", "_", folderName);
        final File file = new File(path);

        mockery.checking(new Expectations() {{
            allowing(fs).createFile(path);
            will(returnValue(file));

            allowing(fs).exists(file);
            will(returnValue(true));

            allowing(fs).isDirectory(file);
            will(returnValue(true));

            allowing(fs).getName(file);
            will(returnValue(folderName));
        }});

        Folder item = (Folder) extractor.get(path);
        assertThat(item, equalTo(new Folder(path, folderName)));
    }

    @Test
    public void bookFilePathReturnsBookItemWithProperName() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Геном";
        final String fileName = bookName + ".fb2.zip";
        final String path = PathUtils.createPath("ru", "Л", author, fileName);

        final File file = new File(path);

        mockery.checking(new Expectations() {{
            allowing(fs).createFile(path);
            will(returnValue(file));

            allowing(fs).exists(file);
            will(returnValue(true));

            allowing(fs).isDirectory(file);
            will(returnValue(false));

            allowing(fs).getName(file);
            will(returnValue(fileName));
        }});

        Book item = (Book) extractor.get(path);
        assertThat(item, equalTo(new Book(path, bookName)));
    }

    @Test
    public void bookFilePathWithSeriesInItWillReturnBookWithSeriesInItsName() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Калеки";
        String seriesName = "Геном";
        final String fileName = bookName + ".fb2.zip";
        final String path = PathUtils.createPath("ru", "Л", author, seriesName, fileName);

        final File file = new File(path);

        mockery.checking(new Expectations() {{
            allowing(fs).createFile(path);
            will(returnValue(file));

            allowing(fs).exists(file);
            will(returnValue(true));

            allowing(fs).isDirectory(file);
            will(returnValue(false));

            allowing(fs).getName(file);
            will(returnValue(fileName));
        }});

        Book item = (Book) extractor.get(path);
        assertThat(item.name, hasWords(seriesName));
    }

}
