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
import java.util.*;

public class LuceneBookIndex implements BookIndex {

    private static final Version LUCENE_VERSION = Version.LUCENE_31;
    private static final int QUERY_LIMIT = 10;

    private final Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);
    private final IndexWriter writer;
    private final Directory dir;

    public LuceneBookIndex(Directory dir) {
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
    public void addBook(Book book) throws IOException {
        Document doc = new Document();

        Field pathField = new Field("path", book.path, Field.Store.YES, Field.Index.NO);
        doc.add(pathField);

        Field nameField = new Field("name", book.name, Field.Store.YES, Field.Index.ANALYZED);
        doc.add(nameField);

        if (book.seriesName != null) {
            Field seriesNameField = new Field("seriesName", book.seriesName, Field.Store.YES, Field.Index.ANALYZED);
            doc.add(seriesNameField);
        }

        writer.updateDocument(new Term("path", book.path), doc);
        writer.commit();
    }

    @Override
    public Set<Book> find(String bookName) throws ParseException, IOException {
        IndexSearcher searcher = new IndexSearcher(dir);

        SortedSet<Book> booksFound = new TreeSet<Book>();
        try {
            booksFound.addAll(searchField(bookName, searcher, "name"));
            booksFound.addAll(searchField(bookName, searcher, "seriesName"));
        } finally {
            searcher.close();
        }

        return booksFound;
    }

    private List<Book> searchField(String bookName, IndexSearcher searcher, String searchFiels) throws ParseException, IOException {
        List<Book> booksFound;
        QueryParser parser = new QueryParser(LUCENE_VERSION, searchFiels, analyzer);
        Query query = parser.parse(bookName);
        TopDocs docs = searcher.search(query, null, QUERY_LIMIT);

        booksFound = new ArrayList<Book>(Math.min(docs.totalHits, QUERY_LIMIT));
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            booksFound.add(createBook(searcher.doc(scoreDoc.doc)));
        }
        return booksFound;
    }

    private Book createBook(Document doc) {
        String name = doc.get("name");
        String path = doc.get("path");
        String seriesName = doc.get("seriesName");
        return new Book(path, name, seriesName);
    }
}
