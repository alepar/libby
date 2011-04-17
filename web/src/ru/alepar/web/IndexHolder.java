package ru.alepar.web;

import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.lib.index.*;
import ru.alepar.lib.traum.FileFeeder;
import ru.alepar.lib.traum.TraumBookInfoExtractor;
import ru.alepar.lib.traum.TraumIndexer;

import java.io.File;
import java.util.ResourceBundle;

public class IndexHolder {

    private static final Logger log = LoggerFactory.getLogger(IndexHolder.class);

    private final static BookIndex bookIndex = new LuceneBookIndex(new RAMDirectory());
    private final static AuthorIndex authorIndex = new LuceneAuthorIndex(new RAMDirectory());
    private final static TraumBookInfoExtractor extractor = new TraumBookInfoExtractor();

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("/libby");
        String traumRoot = bundle.getString("traum.root");
        log.debug("traum.root = {}", traumRoot);

        Iterable<String> feeder = new FileFeeder(new File(traumRoot));
        TraumIndexer indexer = new TraumIndexer(feeder, bookIndex, authorIndex, extractor);
        indexer.go();
    }

    public static Result query(String query) {
        throw new RuntimeException("alepar havent implemented me yet");
    }

    public static class Result {
        public final Author[] authors;
        public final Book[] books;

        public Result(Author[] authors, Book[] books) {
            this.authors = authors;
            this.books = books;
        }
    }

    public static void main(String[] args) {
        System.out.println("hey");
    }

}
