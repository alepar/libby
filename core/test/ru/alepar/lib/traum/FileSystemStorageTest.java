package ru.alepar.lib.traum;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.alepar.lib.fs.FileSystem;
import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Book;
import ru.alepar.lib.model.Folder;

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

        mockery.checking(new Expectations() {{
            allowing(fs).exists(path);
            will(returnValue(true));

            allowing(fs).isDirectory(path);
            will(returnValue(true));

            allowing(fs).getName(path);
            will(returnValue(author));
        }});

        Author item = (Author) extractor.get(path);
        assertThat(item, equalTo(new Author(path, author)));
    }

    @Test
    public void shortFolderPathReturnsFolder() throws Exception {
        final String folderName = "Л";
        final String path = PathUtils.createPath("ru", folderName);

        mockery.checking(new Expectations() {{
            allowing(fs).exists(path);
            will(returnValue(true));

            allowing(fs).isDirectory(path);
            will(returnValue(true));

            allowing(fs).getName(path);
            will(returnValue(folderName));
        }});

        Folder item = (Folder) extractor.get(path);
        assertThat(item, equalTo(new Folder(path, folderName)));
    }

    @Test
    public void underscoreFolderPathReturnsFolder() throws Exception {
        final String folderName = "_scifi";
        final String path = PathUtils.createPath("ru", "_", folderName);

        mockery.checking(new Expectations() {{
            allowing(fs).exists(path);
            will(returnValue(true));

            allowing(fs).isDirectory(path);
            will(returnValue(true));

            allowing(fs).getName(path);
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

        mockery.checking(new Expectations() {{
            allowing(fs).exists(path);
            will(returnValue(true));

            allowing(fs).isDirectory(path);
            will(returnValue(false));

            allowing(fs).getName(path);
            will(returnValue(fileName));
        }});

        Book item = (Book) extractor.get(path);
        assertThat(item, equalTo(new Book(path, bookName)));
    }

    @Test
    public void bookFilePathWithSeriesInItWillReturnBookWithSeriesInItsName() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко 3 Калеки";
        String seriesName = "Геном";
        final String fileName = bookName + ".fb2.zip";
        final String path = PathUtils.createPath("ru", "Л", author, seriesName, fileName);

        mockery.checking(new Expectations() {{
            allowing(fs).exists(path);
            will(returnValue(true));

            allowing(fs).isDirectory(path);
            will(returnValue(false));

            allowing(fs).getName(path);
            will(returnValue(fileName));
        }});

        Book item = (Book) extractor.get(path);
        assertThat(item.name, hasWords(seriesName));
    }

}
