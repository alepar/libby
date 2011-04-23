package ru.alepar.lib.index;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LuceneIndexTest {

    private final Index index = new LuceneIndex(new RAMDirectory());

    @Test
    public void findsBookIfQueriedExactlyByItsName() throws Exception {
        index.addPath("bookLong", "LoooongBookName");

        SortedSet<String> paths = index.find("LoooongBookName");
        assertThat(paths, equalTo(createSet("bookLong")));
    }

    @Test
    public void findsBooksIfQueriedByOneWordFromItsName() throws Exception {
        index.addPath("bookLong", "LoooongBookName");
        index.addPath("bookLongTwo", "Long BookName");
        index.addPath("bookVeryLongTwo", "Very Long BookName");

        SortedSet<String> paths = index.find("Long");
        assertThat(paths, equalTo(createSet("bookLongTwo", "bookVeryLongTwo")));
    }

    @Test
    public void findsBooksIfQueriedByWildcard() throws Exception {
        index.addPath("bookOne", "BookOne");
        index.addPath("bookTwo", "BookTwo");
        index.addPath("bookNot", "NotBook");

        SortedSet<String> paths = index.find("Book*");
        assertThat(paths, equalTo(createSet("bookOne", "bookTwo")));
    }

    @Test
    public void findsBooksIfOrderOfWordsIsSwapped() throws Exception {
        index.addPath("bookLongTwo", "Long BookName");

        SortedSet<String> paths = index.find("bookname long");
        assertThat(paths, equalTo(createSet("bookLongTwo")));
    }

    @Test
    public void findsBooksIgnoringCase() throws Exception {
        index.addPath("bookCaseOne", "boOK");
        index.addPath("bookCaseTwo", "bOOk");
        index.addPath("bookCaseNot", "Nook");

        SortedSet<String> paths = index.find("book");
        assertThat(paths, equalTo(createSet("bookCaseOne", "bookCaseTwo")));
    }

    private static SortedSet<String> createSet(String... paths) {
        return new TreeSet<String>(Arrays.asList(paths));
    }

}
