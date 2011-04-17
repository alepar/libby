package ru.alepar.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.lib.index.*;
import ru.alepar.lib.traum.FileFeeder;
import ru.alepar.lib.traum.TraumBookInfoExtractor;
import ru.alepar.lib.traum.TraumIndexer;

import java.io.File;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

public class IndexHolder {

    private static final Logger log = LoggerFactory.getLogger(IndexHolder.class);

    private static BookIndex bookIndex;
    private static AuthorIndex authorIndex;
    private final static TraumBookInfoExtractor extractor = new TraumBookInfoExtractor();

    static {
        try {
            Settings settings = new ResourceSettings(ResourceBundle.getBundle("/libby"));
            log.info("traum.root = {}", settings.traumRoot());

            IndexFactory indexFactory;
            if (settings.traumIndex() != null) {
                log.info("storing index to {}", settings.traumIndex());
                indexFactory = new FSIndexFactory(settings.traumIndex());
            } else {
                log.info("storring index to RAM");
                indexFactory = new RAMIndexFactory();
            }

            bookIndex = indexFactory.createBookIndex();
            authorIndex = indexFactory.createAuthorIndex();

            if (settings.reindex()) {
                log.info("reindexing");
                Date start = new Date();
                Iterable<String> feeder = new FileFeeder(new File(settings.traumRoot()));
                TraumIndexer indexer = new TraumIndexer(feeder, bookIndex, authorIndex, extractor);
                indexer.go();
                Date end = new Date();
                log.info("reindex took {}s, added {} files", (end.getTime() - start.getTime()) / 1000, indexer.getCounter());
            } else {
                log.warn("skipping reindex");
            }
        } catch (Exception e) {
            log.error("failed to bring up libby, terminating", e);
            System.exit(-1);
        }
    }

    public static Result query(String query) {
        try {
            Set<Book> books = bookIndex.find(query);
            Set<Author> authors = authorIndex.find(query);
            return new Result(
                    authors.toArray(new Author[authors.size()]),
                    books.toArray(new Book[books.size()])
            );
        } catch (Exception e) {
            log.warn("query failed: " + query, e);
            throw new RuntimeException("query failed: " + query, e);
        }
    }

    public static class Result {
        public final Author[] authors;
        public final Book[] books;

        public Result(Author[] authors, Book[] books) {
            this.authors = authors;
            this.books = books;
        }
    }

}
