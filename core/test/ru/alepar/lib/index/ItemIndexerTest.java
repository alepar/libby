package ru.alepar.lib.index;

import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Book;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static ru.alepar.lib.traum.PathUtils.createPath;
import static ru.alepar.testsupport.MyMatchers.hasWords;

public class ItemIndexerTest {

    @Rule
    public final JUnitRuleMockery mockery = new JUnitRuleMockery();

    private final Index index = mockery.mock(Index.class);
    private final FileCounter counter = mockery.mock(FileCounter.class);

    private final ItemIndexer indexer = new ItemIndexer(index, counter);

    @Test
    public void bookGetsIndexedByItsName() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко - Геном";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            oneOf(index).addPath(with(equalTo(path)), with(hasWords(
                    "Геном"
            )), with(Matchers.<Double>equalTo(null)));
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
            oneOf(index).addPath(with(equalTo(path)), with(hasWords(
                    series
            )), with(Matchers.<Double>equalTo(null)));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void bookGetsIndexedByCoAuthor() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко, Перумов - Не время для драконов";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            oneOf(index).addPath(with(equalTo(path)), with(hasWords(
                    "Перумов"
            )), with(Matchers.<Double>equalTo(null)));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void bookWithNoAuthorInfoGetsWholePathWithClearedOffNonWordCharactersIntoIndex() throws Exception {
        String bookName = "Журнал PC Magazine_RE #01_2009.fb2.zip";
        final String path = createPath("ru", "_", "_периодика", "Журнал PC Magazine", bookName);

        mockery.checking(new Expectations() {{
            oneOf(index).addPath(with(equalTo(path)), with(hasWords(
                    "ru", "периодика", "Журнал", "PC", "Magazine", "RE", "01", "2009"
            )), with(Matchers.<Double>equalTo(null)));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void includesAuthorNameInBookIndex() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко - Геном";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            oneOf(index).addPath(with(equalTo(path)), with(hasWords("Лукьяненко")), with(Matchers.<Double>equalTo(null)));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void doesNotExcludeAuthorNameFromBookIndexIfItComesAfterDash() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Лукьяненко - Сергей Геном";
        final String path = createPath("ru", "Л", author, bookName + ".fb2.zip");

        mockery.checking(new Expectations() {{
            oneOf(index).addPath(with(equalTo(path)), with(hasWords("Сергей")), with(Matchers.<Double>equalTo(null)));
        }});

        indexer.onBook(new Book(path, bookName));
    }

    @Test
    public void authorGetsIndexedByItsName() throws Exception {
        String author = "Лукьяненко Сергей";
        final String path = createPath("ru", "Л", author);

        mockery.checking(new Expectations() {{
            oneOf(index).addPath(with(equalTo(path)), with(hasWords(
                    "Лукьяненко", "Сергей"
            )), with(any(Double.class)));

            allowing(counter).count(with(any(String.class)));
            will(returnValue(0));
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
            oneOf(index).addPath(with(equalTo(path)), with(allOf(
                    hasWords(series),
                    hasWords("Лукьяненко")
            )), with(Matchers.<Double>equalTo(null)));
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
            oneOf(index).addPath(with(equalTo(path)), with(
                    hasWords("Танцы", "на", "снегу")
            ), with(Matchers.<Double>equalTo(null)));
        }});

        indexer.onBook(new Book(path, bookName));
    }


}
