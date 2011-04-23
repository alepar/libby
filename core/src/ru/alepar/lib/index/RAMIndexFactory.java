package ru.alepar.lib.index;

import org.apache.lucene.store.RAMDirectory;

public class RAMIndexFactory implements IndexFactory {

    @Override
    public Index createIndex() {
        return new LuceneIndex(new RAMDirectory());
    }

}
