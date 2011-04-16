package ru.alepar.lib.traum;

import org.apache.lucene.store.RAMDirectory;
import ru.alepar.lib.index.BookIndex;
import ru.alepar.lib.index.LuceneBookIndex;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class TraumIndexer {

    private final File traumDir;
    private final BookIndex bookIndex;
    private final TraumBookInfoExtractor extractor;

    public TraumIndexer(File traumDir, BookIndex bookIndex, TraumBookInfoExtractor extractor) {
        this.traumDir = traumDir;
        this.bookIndex = bookIndex;
        this.extractor = extractor;
    }

    public void go() {
        recurse(traumDir);
    }

    private void recurse(File dir) {
        String[] childNames = dir.list();
        for (String childName : childNames) {
            File child = new File(dir, childName);
            if (child.isDirectory()) {
                recurse(child);
            }
            if (child.isFile()) {
                try {
                    TraumBookInfoExtractor.Info info = extractor.extract(chopOffTraumDir(child.getCanonicalPath(), traumDir.getCanonicalPath()));
                    bookIndex.addBook(info.book);
                } catch (IOException e) {
                    throw new RuntimeException("ok, my code is shit", e);
                }
            }
        }
    }

    private String chopOffTraumDir(String path, String pathToChop) {
        return path.substring(pathToChop.length());
    }

    public static void main(String[] args) {
        Date start = new Date();
        LuceneBookIndex bookIndex = new LuceneBookIndex(new RAMDirectory());
        TraumIndexer indexer = new TraumIndexer(new File("f:\\"), bookIndex, new TraumBookInfoExtractor());
        indexer.go();
        Date end = new Date();
        System.out.println((end.getTime() - start.getTime()) / 1000);
        System.out.println("Query time");
    }

}
