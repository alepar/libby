package ru.alepar.lib.traum;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.alepar.lib.index.AuthorIndex;
import ru.alepar.lib.index.BookIndex;
import ru.alepar.lib.model.Author;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@RunWith(JMock.class)
public class TraumIndexerTest {

    private final Mockery mockery = new JUnit4Mockery();
    private final BookIndex bookIndex = mockery.mock(BookIndex.class);
    private final AuthorIndex authorIndex = mockery.mock(AuthorIndex.class);

    @Test
    public void authorGetsItsWayIntoAuthorIndex() throws Exception {
        mockery.checking(new Expectations() {{
            ignoring(bookIndex);

            allowing(authorIndex).addAuthor(createAuthor("ru", "Г", "Громов Александр"));
            allowing(authorIndex).addAuthor(createAuthor("ru", "Г", "Громов Сергей"));
            allowing(authorIndex).addAuthor(createAuthor("ru", "Г", "Громов Алексей"));
        }});
        List<String> files = Arrays.asList(
                concatClean("ru", "Г", "Громов Александр", "smth.fb2.zip"),
                concatClean("ru", "Г", "Громов Сергей", "smthelse.fb2.zip"),
                concatClean("ru", "Г", "Громов Алексей", "Геном", "smthelse.fb2.zip")
        );
        TraumIndexer indexer = createIndexer(files);
        indexer.go();
    }

    private TraumIndexer createIndexer(List<String> files) {
        return new TraumIndexer(files, bookIndex, authorIndex, new TraumBookInfoExtractor());
    }

    private static Author createAuthor(String... names) {
        return new Author(concat(names), names[names.length-1]);
    }

    private static String concat(String... names) {
        StringBuilder result = new StringBuilder();
        for (String name : names) {
            result.append(name).append(File.separatorChar);
        }
        return result.toString();
    }

    private static String concatClean(String... names) {
        StringBuilder result = new StringBuilder();
        for (String name : names) {
            if(result.length() > 0) {
                result.append(File.separatorChar);
            }
            result.append(name);
        }
        return result.toString();
    }
}
