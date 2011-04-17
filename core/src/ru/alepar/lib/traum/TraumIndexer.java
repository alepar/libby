package ru.alepar.lib.traum;

import ru.alepar.lib.index.AuthorIndex;
import ru.alepar.lib.index.BookIndex;

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

}
