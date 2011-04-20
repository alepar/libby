package ru.alepar.lib.index;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import ru.alepar.lib.model.Book;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LuceneBookIndexTest {

    private final BookIndex index = new LuceneBookIndex(new RAMDirectory());

    private final Book bookLong = createBook("somefile", "LoooongBookName", null);

    private final Book bookLongTwo = createBook("somefile", "Long BookName", null);
    private final Book bookVeryLongTwo = createBook("somefile", "Very Long BookName", null);

    private final Book bookOne = createBook("somefile", "BookOne", null);
    private final Book bookTwo = createBook("somefile", "BookTwo", null);
    private final Book bookNot = createBook("somefile", "NotBook", null);

    private final Book bookCaseOne = createBook("somefile", "boOK", null);
    private final Book bookCaseTwo = createBook("somefile", "bOOk", null);
    private final Book bookCaseNot = createBook("somefile", "Nook", null);

    private final Book bookSeriesOne = createBook("somefile", "", "series one");
    private final Book bookSeriesTwo = createBook("somefile", "", "series two");


    @Test
    public void findsBookIfQueriedExactlyByItsName() throws Exception {
        index.addBook(bookLong);

        Set<Book> foundBooks = index.find("LoooongBookName");
        assertThat(foundBooks, equalTo(createSet(bookLong)));
    }

    @Test
    public void findsBooksIfQueriedByOneWordFromItsName() throws Exception {
        index.addBook(bookLong);
        index.addBook(bookLongTwo);
        index.addBook(bookVeryLongTwo);

        Set<Book> foundBooks = index.find("Long");
        assertThat(foundBooks, equalTo(createSet(bookVeryLongTwo, bookLongTwo)));
    }

    @Test
    public void findsBooksIfQueriedByWildcard() throws Exception {
        index.addBook(bookOne);
        index.addBook(bookTwo);
        index.addBook(bookNot);

        Set<Book> foundBooks = index.find("Book*");
        assertThat(foundBooks, equalTo(createSet(bookOne, bookTwo)));
    }

    @Test
    public void findsBooksIfOrderOfWordsIsSwapped() throws Exception {
        index.addBook(bookLongTwo);

        Set<Book> foundBooks = index.find("bookname long");
        assertThat(foundBooks, equalTo(createSet(bookLongTwo)));
    }

    @Test
    public void findsBooksBySeries() throws Exception {
        index.addBook(bookSeriesOne);
        index.addBook(bookSeriesTwo);

        Set<Book> foundBooks = index.find("series");
        assertThat(foundBooks, equalTo(createSet(bookSeriesOne, bookSeriesTwo)));
    }

    @Test
    public void findsBooksIgnoringCase() throws Exception {
        index.addBook(bookCaseOne);
        index.addBook(bookCaseTwo);
        index.addBook(bookCaseNot);

        Set<Book> foundBooks = index.find("book");
        assertThat(foundBooks, equalTo(createSet(bookCaseOne, bookCaseTwo)));
    }

    private static Book createBook(String fileName, String bookName, String seriesName) {
        return new Book(fileName, bookName, seriesName);
    }

    private static Set<Book> createSet(Book... books) {
        return new TreeSet<Book>(Arrays.asList(books));
    }

}
