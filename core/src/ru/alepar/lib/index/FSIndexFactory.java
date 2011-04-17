package ru.alepar.lib.index;

import org.apache.lucene.store.FSDirectory;
import ru.alepar.lib.stuff.Oops;

import java.io.File;

public class FSIndexFactory implements IndexFactory {
    private final File basePath;

    public FSIndexFactory(String path) {
        basePath = new File(path);
    }

    @Override
    public BookIndex createBookIndex() {
        try {
            return new LuceneBookIndex(FSDirectory.open(new File(basePath, "books")));
        } catch (Exception e) {
            throw new Oops(e);
        }
    }

    @Override
    public AuthorIndex createAuthorIndex() {
        try {
            return new LuceneAuthorIndex(FSDirectory.open(new File(basePath, "authors")));
        } catch (Exception e) {
            throw new Oops(e);
        }
    }
}
