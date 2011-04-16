package ru.alepar.lib.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;

public abstract class LuceneIndex {

    private static final Version LUCENE_VERSION = Version.LUCENE_31;
    protected static final int QUERY_LIMIT = 10;

    private final Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);
    private final IndexWriter writer;
    private final Directory dir;

    public LuceneIndex(Directory dir) {
        this.dir = dir;
        try {
            IndexWriterConfig iwc = new IndexWriterConfig(LUCENE_VERSION, analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer = new IndexWriter(dir, iwc);
        } catch (Exception e) {
            throw new RuntimeException("failed to open lucene undex", e);
        }
    }

    protected IndexSearcher searcher() throws IOException {
        return new IndexSearcher(dir);
    }

    protected QueryParser parser(String searchField) {
        return new QueryParser(LUCENE_VERSION, searchField, analyzer);
    }

    protected IndexWriter writer() {
        return writer;
    }

}
