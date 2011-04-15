package ru.alepar.lib.index;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LuceneBookIndexTest {

    private final BookIndex index =  new LuceneBookIndex(new RAMDirectory());

    private final Book bookLong = createBook("somefile", "LoooongBookName");

    private final Book bookLongTwo = createBook("somefile", "Long BookName");
    private final Book bookVeryLongTwo = createBook("somefile", "Very Long BookName");

    private final Book bookOne = createBook("somefile", "BookOne");
    private final Book bookTwo = createBook("somefile", "BookTwo");
    private final Book bookNot = createBook("somefile", "NotBook");

    private final Book bookCaseOne = createBook("somefile", "boOK");
    private final Book bookCaseTwo = createBook("somefile", "bOOk");
    private final Book bookCaseNot = createBook("somefile", "Nook");



    @Test
    public void findsBookIfQueriedExactlyByItsName() throws Exception {
        index.addBook(bookLong);

        List<Book> foundBooks = index.find("LoooongBookName");
        assertThat(foundBooks, equalTo(Collections.singletonList(bookLong)));
    }

    @Test
    public void findsBooksIfQueriedByOneWordFromItsName() throws Exception {
        index.addBook(bookLong);
        index.addBook(bookLongTwo);
        index.addBook(bookVeryLongTwo);

        List<Book> foundBooks = index.find("Long");
        assertThat(foundBooks, equalTo(Arrays.asList(bookLongTwo, bookVeryLongTwo)));
    }

    @Test
    public void findsBooksIfQueriedByWildcard() throws Exception {
        index.addBook(bookOne);
        index.addBook(bookTwo);
        index.addBook(bookNot);

        List<Book> foundBooks = index.find("Book*");
        assertThat(foundBooks, equalTo(Arrays.asList(bookOne, bookTwo)));
    }

    @Test
    public void findsBooksIfOrderOfWordsIsSwapped() throws Exception {
        index.addBook(bookLongTwo);

        List<Book> foundBooks = index.find("bookname long");
        assertThat(foundBooks, equalTo(Arrays.asList(bookLongTwo)));
    }

    @Test
    public void findsBooksIgnoringCase() throws Exception {
        index.addBook(bookCaseOne);
        index.addBook(bookCaseTwo);
        index.addBook(bookCaseNot);

        List<Book> foundBooks = index.find("book");
        assertThat(foundBooks, equalTo(Arrays.asList(bookCaseOne, bookCaseTwo)));
    }

    private static Book createBook(String fileName, String bookName) {
        return new Book(fileName, bookName);
    }

}
