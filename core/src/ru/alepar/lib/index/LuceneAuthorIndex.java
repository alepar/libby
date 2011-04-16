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

public class LuceneAuthorIndex extends LuceneIndex implements AuthorIndex {

    public LuceneAuthorIndex(Directory dir) {
        super(dir);
    }

    @Override
    public void addAuthor(Author author) throws IOException {
        Document doc = new Document();

        Field pathField = new Field("path", author.path, Field.Store.YES, Field.Index.NO);
        doc.add(pathField);

        Field nameField = new Field("name", author.name, Field.Store.YES, Field.Index.ANALYZED);
        doc.add(nameField);

        writer().updateDocument(new Term("path", author.path), doc);
        writer().commit();
    }

    @Override
    public Set<Author> find(String authorName) throws ParseException, IOException {
        IndexSearcher searcher = searcher();

        SortedSet<Author> authorsFound = new TreeSet<Author>();
        try {
            authorsFound.addAll(searchField(authorName, searcher, "name"));
        } finally {
            searcher.close();
        }

        return authorsFound;
    }

    private List<Author> searchField(String authorName, IndexSearcher searcher, String searchField) throws ParseException, IOException {
        List<Author> authorsFound;
        Query query = parser(searchField).parse(authorName);
        TopDocs docs = searcher.search(query, null, QUERY_LIMIT);

        authorsFound = new ArrayList<Author>(Math.min(docs.totalHits, QUERY_LIMIT));
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            authorsFound.add(createAuthor(searcher.doc(scoreDoc.doc)));
        }
        return authorsFound;
    }

    private Author createAuthor(Document doc) {
        String name = doc.get("name");
        String path = doc.get("path");
        return new Author(path, name);
    }
}
