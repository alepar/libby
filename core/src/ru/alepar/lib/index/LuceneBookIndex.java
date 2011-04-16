package ru.alepar.lib.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.*;

public class LuceneBookIndex extends LuceneIndex implements BookIndex {

    public LuceneBookIndex(Directory dir) {
        super(dir);
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

        writer().updateDocument(new Term("path", book.path), doc);
        writer().commit();
    }

    @Override
    public Set<Book> find(String bookName) throws ParseException, IOException {
        IndexSearcher searcher = searcher();

        SortedSet<Book> booksFound = new TreeSet<Book>();
        try {
            booksFound.addAll(searchField(bookName, searcher, "name"));
            booksFound.addAll(searchField(bookName, searcher, "seriesName"));
        } finally {
            searcher.close();
        }

        return booksFound;
    }

    private List<Book> searchField(String bookName, IndexSearcher searcher, String searchField) throws ParseException, IOException {
        List<Book> booksFound;
        Query query = parser(searchField).parse(bookName);
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
