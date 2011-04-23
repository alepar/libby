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
import static ru.alepar.testsupport.MyMatchers.hasWords;

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
            one(index).addPath(with(equalTo(path)), with(hasWords(
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
            one(index).addPath(with(equalTo(path)), with(hasWords(
                    series
            )));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void bookGetsIndexedByCoAuthor() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко, Перумов - Не время для драконов";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasWords(
                    "Перумов"
            )));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void bookWithNoAuthorInfoGetsWholePathWithClearedOffNonWordCharactersIntoIndex() throws Exception {
        String bookName = "Журнал PC Magazine_RE #01_2009.fb2.zip";
        final String path = createPath("ru", "_", "_периодика", "Журнал PC Magazine", bookName);

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasWords(
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
            one(index).addPath(with(equalTo(path)), with(not(hasWords("Лукьяненко"))));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void doesNotExcludeAuthorNameFromBookIndexIfItComesAfterDash() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко - Сергей Геном";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasWords("Сергей")));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void authorGetsIndexedByItsName() throws Exception {
        String author = "Лукьяненко Сергей";
        final String path = createPath("ru", "Л", author);

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(hasWords(
                    "Лукьяненко", "Сергей"
            )));
        }});

        indexer.onAuthor(new Author(path, author));
    }

    @Test
    public void dealsFineWhenBookNameDoesNotContainDashes() throws Exception {
        String author = "Лукьяненко Сергей";
        final String series = "Геном";
        String bookName = "Лукьяненко 1 Танцы на снегу";
        final String path = createPath("ru", "Л", author, series, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(allOf(
                    hasWords(series),
                    not(hasWords("Лукьяненко"))
            )));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void dealsFineWhenBookNameContainsDots() throws Exception {
        String bookName = "Горин - ...Чума на оба ваши дома!";
        final String path = createPath("ru", "Г", "Горин Григорий", bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            ignoring(index);
        }});

        indexer.onBook(new Book(path, bookName));
        //just check that no exceptions are thrown
    }

    @Test
    public void addsToIndexPartOfBookNameWhichComesAfterDot() throws Exception {
        String author = "Лукьяненко Сергей";
        final String series = "Геном";
        String bookName = "Танцы .на снегу";
        final String path = createPath("ru", "Л", author, series, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            one(index).addPath(with(equalTo(path)), with(
                    hasWords("Танцы", "на", "снегу")
            ));
        }});

        indexer.onBook(new Book(path, bookName));
    }


}
