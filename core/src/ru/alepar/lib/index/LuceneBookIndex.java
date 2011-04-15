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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        writer.updateDocument(new Term("path", book.path), doc);
        writer.commit();
    }

    @Override
    public List<Book> find(String bookName) throws ParseException, IOException {
        IndexSearcher searcher = new IndexSearcher(dir);
        QueryParser parser = new QueryParser(LUCENE_VERSION, "name", analyzer);
        Query query = parser.parse(bookName);
        TopDocs docs = searcher.search(query, null, QUERY_LIMIT);

        List<Book> booksFound = new ArrayList<Book>(Math.min(docs.totalHits, QUERY_LIMIT));
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String name = doc.get("name");
            String path = doc.get("path");
            booksFound.add(new Book(path, name));
        }

        return booksFound;
    }
}
