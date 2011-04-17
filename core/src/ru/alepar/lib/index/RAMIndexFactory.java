package ru.alepar.lib.index;

import org.apache.lucene.store.RAMDirectory;

public class RAMIndexFactory implements IndexFactory {

    @Override
    public BookIndex createBookIndex() {
        return new LuceneBookIndex(new RAMDirectory());
    }

    @Override
    public AuthorIndex createAuthorIndex() {
        return new LuceneAuthorIndex(new RAMDirectory());
    }
}
