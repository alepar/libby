package ru.alepar.lib.traum;

import org.apache.lucene.store.RAMDirectory;
import ru.alepar.lib.index.AuthorIndex;
import ru.alepar.lib.index.BookIndex;
import ru.alepar.lib.index.LuceneAuthorIndex;
import ru.alepar.lib.index.LuceneBookIndex;

import java.io.File;
import java.util.Date;

public class TraumIndexer {

    private final Iterable<String> feeder;
    private final BookIndex bookIndex;
    private final AuthorIndex authorIndex;
    private final TraumBookInfoExtractor extractor;
    private int counter;

    public TraumIndexer(Iterable<String> feeder, BookIndex bookIndex, AuthorIndex authorIndex, TraumBookInfoExtractor extractor) {
        this.feeder = feeder;
        this.bookIndex = bookIndex;
        this.authorIndex = authorIndex;
        this.extractor = extractor;
    }

    public void go() {
        for (String filePath : feeder) {
            counter++;
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

    public int getCounter() {
        return counter;
    }

    //    Kindle/2.5 screen 824x1200
    public static void main(String[] args) throws Exception {
        Date start = new Date();
        BookIndex bookIndex = new LuceneBookIndex(new RAMDirectory());
        AuthorIndex authorIndex = new LuceneAuthorIndex(new RAMDirectory());
        Iterable<String> feeder = new FileFeeder(new File("f:\\traum"));
        TraumIndexer indexer = new TraumIndexer(feeder, bookIndex, authorIndex, new TraumBookInfoExtractor());
        indexer.go();
        Date end = new Date();
        System.out.println(String.format("Build took %d, indexed %d files", (end.getTime() - start.getTime()) / 1000, indexer.counter));
    }

}
