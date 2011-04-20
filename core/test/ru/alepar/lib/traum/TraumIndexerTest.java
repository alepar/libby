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

            allowing(authorIndex).addAuthor(new Author("ru\\Г\\Громов Александр\\", "Громов Александр"));
            allowing(authorIndex).addAuthor(new Author("ru\\Г\\Громов Сергей\\", "Громов Сергей"));
            allowing(authorIndex).addAuthor(new Author("ru\\Г\\Громов Алексей\\", "Громов Алексей"));
        }});
        List<String> files = Arrays.asList(
                "ru\\Г\\Громов Александр\\smth.fb2.zip",
                "ru\\Г\\Громов Сергей\\smthelse.fb2.zip",
                "ru\\Г\\Громов Алексей\\Геном\\smthelse.fb2.zip"
        );
        TraumIndexer indexer = createIndexer(files);
        indexer.go();
    }

    private TraumIndexer createIndexer(List<String> files) {
        return new TraumIndexer(files, bookIndex, authorIndex, new TraumBookInfoExtractor());
    }
}
