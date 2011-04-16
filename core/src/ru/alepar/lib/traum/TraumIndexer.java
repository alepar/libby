package ru.alepar.lib.traum;

import org.apache.lucene.store.RAMDirectory;
import ru.alepar.lib.index.*;

import java.io.File;
import java.util.Date;
import java.util.Set;

public class TraumIndexer {

    private final Iterable<String> feeder;
    private final BookIndex bookIndex;
    private final AuthorIndex authorIndex;
    private final TraumBookInfoExtractor extractor;

    public TraumIndexer(Iterable<String> feeder, BookIndex bookIndex, AuthorIndex authorIndex, TraumBookInfoExtractor extractor) {
        this.feeder = feeder;
        this.bookIndex = bookIndex;
        this.authorIndex = authorIndex;
        this.extractor = extractor;
    }

    public void go() {
        for (String filePath : feeder) {
            try {
                TraumBookInfoExtractor.Info info = extractor.extract(filePath);
                bookIndex.addBook(info.book);
                if (info.author != null) {
                    authorIndex.addAuthor(info.author);
                }
            } catch (Exception e) {
                throw new RuntimeException("failed to index traum lib", e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Date start = new Date();
        BookIndex bookIndex = new LuceneBookIndex(new RAMDirectory());
        AuthorIndex authorIndex = new LuceneAuthorIndex(new RAMDirectory());
        Iterable<String> feeder = new FileFeeder(new File("f:\\traum"));
        TraumIndexer indexer = new TraumIndexer(feeder, bookIndex, authorIndex, new TraumBookInfoExtractor());
        indexer.go();
        Date end = new Date();
        System.out.println((end.getTime() - start.getTime()) / 1000);
        System.out.println("Query time");
        Set<Author> authors = authorIndex.find("фр*");
        System.out.println(authors);
    }

}
