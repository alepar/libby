package ru.alepar.lib.index;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LuceneIndexTest {

    private final Index index = new LuceneIndex(new RAMDirectory());

    @Test
    public void findsBookIfQueriedExactlyByItsName() throws Exception {
        index.addPath("bookLong", "LoooongBookName", null);

        List<String> paths = index.find("LoooongBookName");
        assertThat(paths, equalTo(createList("bookLong")));
    }

    @Test
    public void findsBooksIfQueriedByOneWordFromItsName() throws Exception {
        index.addPath("bookLong", "LoooongBookName", null);
        index.addPath("bookLongTwo", "Long BookName", null);
        index.addPath("bookVeryLongTwo", "Very Long BookName", null);

        List<String> paths = index.find("Long");
        assertThat(paths, equalTo(createList("bookLongTwo", "bookVeryLongTwo")));
    }

    @Test
    public void findsBooksIfQueriedByWildcard() throws Exception {
        index.addPath("bookOne", "BookOne", null);
        index.addPath("bookTwo", "BookTwo", null);
        index.addPath("bookNot", "NotBook", null);

        List<String> paths = index.find("Book*");
        assertThat(paths, equalTo(createList("bookOne", "bookTwo")));
    }

    @Test
    public void findsBooksIfOrderOfWordsIsSwapped() throws Exception {
        index.addPath("bookLongTwo", "Long BookName", null);

        List<String> paths = index.find("bookname long");
        assertThat(paths, equalTo(createList("bookLongTwo")));
    }

    @Test
    public void findsBooksIgnoringCase() throws Exception {
        index.addPath("bookCaseOne", "boOK", null);
        index.addPath("bookCaseTwo", "bOOk", null);
        index.addPath("bookCaseNot", "Nook", null);

        List<String> paths = index.find("book");
        assertThat(paths, equalTo(createList("bookCaseOne", "bookCaseTwo")));
    }

    @Test
    public void pathsAreReturnedInOrderOfScore() throws Exception {
        index.addPath("book3", "book and book aand book", 10.0);
        index.addPath("book2", "book ma book", 5.0);
        index.addPath("book1", "BoOk", null);

        List<String> paths = index.find("book");
        assertThat(paths, equalTo(createList("book3", "book2", "book1")));
    }

    private static List<String> createList(String... paths) {
        return Arrays.asList(paths);
    }

}
