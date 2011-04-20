package ru.alepar.lib.index;

import org.apache.lucene.queryParser.ParseException;
import ru.alepar.lib.model.Author;

import java.io.IOException;
import java.util.Set;

public interface AuthorIndex {

    void addAuthor(Author author) throws IOException;

    Set<Author> find(String authorName) throws ParseException, IOException;
}
