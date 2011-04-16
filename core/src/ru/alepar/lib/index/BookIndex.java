package ru.alepar.lib.index;

import org.apache.lucene.queryParser.ParseException;

import java.io.IOException;
import java.util.Set;

public interface BookIndex {
    void addBook(Book book) throws IOException;

    Set<Book> find(String bookName) throws ParseException, IOException;
}
