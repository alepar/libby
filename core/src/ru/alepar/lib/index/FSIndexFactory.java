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
    public Index createIndex() {
        try {
            return new LuceneIndex(FSDirectory.open(new File(basePath, "books")));
        } catch (Exception e) {
            throw new Oops(e);
        }
    }

}
