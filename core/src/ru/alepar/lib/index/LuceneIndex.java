package ru.alepar.lib.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LuceneIndex implements Index {

    private static final Version LUCENE_VERSION = Version.LUCENE_31;
    private static final int QUERY_LIMIT = 30;

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

    @Override
    public void addPath(String path, String indexWords, Double boost) throws IOException {
        Document doc = new Document();

        Field pathField = new Field("path", path, Field.Store.YES, Field.Index.NO);
        doc.add(pathField);

        Field nameField = new Field("name", indexWords, Field.Store.NO, Field.Index.ANALYZED);
        doc.add(nameField);

        if (boost != null) {
            doc.setBoost(new Float(boost));
        }

        writer().updateDocument(new Term("path", path), doc);
        writer().commit();
    }

    @Override
    public List<String> find(String query) throws ParseException, IOException {
        IndexSearcher searcher = searcher();

        List<String> pathsFound = new ArrayList<String>();
        try {
            pathsFound.addAll(searchField(query, searcher, "name"));
        } finally {
            searcher.close();
        }

        return pathsFound;
    }

    private List<String> searchField(String queryString, IndexSearcher searcher, String searchField) throws ParseException, IOException {
        Query query = parser(searchField).parse(queryString);
        TopDocs docs = searcher.search(query, null, QUERY_LIMIT);

        List<String> results = new ArrayList<String>(docs.totalHits);
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            results.add(doc.get("path"));
        }
        return results;
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
