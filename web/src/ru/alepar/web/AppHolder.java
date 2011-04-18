package ru.alepar.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.ebook.convert.CalibreConverter;
import ru.alepar.ebook.convert.Exec;
import ru.alepar.ebook.convert.RuntimeExec;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.ebook.format.StaticFormatProvider;
import ru.alepar.ebook.format.UserAgentDetector;
import ru.alepar.lib.index.*;
import ru.alepar.lib.traum.FileFeeder;
import ru.alepar.lib.traum.TraumBookInfoExtractor;
import ru.alepar.lib.traum.TraumIndexer;
import ru.alepar.setting.ResourceSettings;
import ru.alepar.setting.Settings;

import java.io.File;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

public class AppHolder {

    private static final Logger log = LoggerFactory.getLogger(AppHolder.class);

    private static final TraumBookInfoExtractor extractor = new TraumBookInfoExtractor();
    private static final Settings settings = new ResourceSettings(ResourceBundle.getBundle("/libby"));
    private static final FormatProvider provider = new StaticFormatProvider();
    private static final Exec exec = new RuntimeExec();
    private static final UserAgentDetector detector = new UserAgentDetector();

    private static BookIndex bookIndex;
    private static AuthorIndex authorIndex;
    private static CalibreConverter converter;


    static {
        try {
            log.info("traum.root = {}", settings.traumRoot());

            instantiateIndexes();
            reindex();

            converter = new CalibreConverter(settings.calibreConvert(), exec, provider);

        } catch (Exception e) {
            log.error("failed to bring up libby, terminating", e);
            System.exit(-1);
        }
    }

    private static void reindex() {
        if (settings.traumReindex()) {
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
    }

    private static void instantiateIndexes() {
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

    public static File convertFile(File in, EbookType type) {
        return converter.convertFor(type, in);
    }

    public static File getFile(String path) {
        return new File(settings.traumRoot(), path);
    }

    public static EbookType detect(String header) {
        return detector.detect(header);
    }

    public static String convertName(String name, EbookType type) {
        if (type == EbookType.UNKNOWN) {
            return name;
        }
        String nameWithoutExt = name.substring(0, name.indexOf("."));
        return String.format("%s.%s", nameWithoutExt, provider.extension(type));
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
