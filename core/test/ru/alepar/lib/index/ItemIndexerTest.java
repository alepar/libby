package ru.alepar.lib.index;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Book;

import static org.hamcrest.Matchers.*;
import static ru.alepar.lib.traum.PathUtils.createPath;
import static ru.alepar.testsupport.MyMatchers.hasSubstrings;

@RunWith(JMock.class)
public class ItemIndexerTest {

    private final Mockery mockery = new JUnit4Mockery();

    private final Index index = mockery.mock(Index.class);
    private final ItemIndexer indexer = new ItemIndexer(index);

    @Test
    public void bookGetsIndexedByItsName() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко - Геном";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasSubstrings(
                    "Геном"
            )));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void bookGetsIndexedByItsSeries() throws Exception {
        String author = "Лукьяненко Сергей";
        final String series = "Геном";
        String bookName = "Лукьяненко - Калеки";
        final String path = createPath("ru", "Л", author, series, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasSubstrings(
                    series
            )));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void bookWithNoAuthorInfoGetsWholePathWithClearedOffNonWordCharactersIntoIndex() throws Exception {
        String bookName = "Журнал PC Magazine_RE #01_2009.fb2.zip";
        final String path = createPath("ru", "_", "_периодика", "Журнал PC Magazine", bookName);

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasSubstrings(
                    "ru", "периодика", "Журнал", "PC", "Magazine", "RE", "01", "2009"
            )));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void excludesAuthorNameFromBookIndex() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко - Геном";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(not(hasSubstrings("Лукьяненко"))));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void doesNotExcludeAuthorNameFromBookIndexIfItComesAfterDash() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко - Сергей Геном";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasSubstrings("Сергей")));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void authorGetsIndexedByItsName() throws Exception {
        String author = "Лукьяненко Сергей";
        final String path = createPath("ru", "Л", author);

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasSubstrings(
                    "Лукьяненко", "Сергей"
            )));
        }});

        indexer.onAuthor(new Author(path, author));
    }


}
