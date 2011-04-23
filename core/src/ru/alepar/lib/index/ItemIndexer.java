package ru.alepar.lib.index;

import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Book;
import ru.alepar.lib.model.Folder;
import ru.alepar.lib.model.ItemVisitor;

import java.io.File;
import java.util.regex.Pattern;

import static ru.alepar.lib.traum.PathUtils.*;

public class ItemIndexer implements ItemVisitor {

    private static final double AUTHOR_BOOST = 1.2;
    private final Index index;

    public ItemIndexer(Index index) {
        this.index = index;
    }

    @Override
    public void onBook(Book book) {
        String[] split = book.path.split(Pattern.quote(File.separator));
        String indexWords;

        if ("_".equals(split[1])) {
            indexWords = extractIndexForBookWithNoAuthor(book.path);
        } else if (split.length == 5) {
            indexWords = extractIndexForBookWithSeriesInfo(split);
        } else {
            indexWords = extractIndexForBookWithNoSeriesInfo(split);
        }

        try {
            index.addPath(book.path, indexWords, null);
        } catch (Exception e) {
            throw new RuntimeException("failed to add book to index", e);
        }
    }

    @Override
    public void onAuthor(Author author) {
        try {
            index.addPath(author.path, author.name, AUTHOR_BOOST);
        } catch (Exception e) {
            throw new RuntimeException("failed to add author to index", e);
        }
    }

    @Override
    public void onFolder(Folder folder) {
        // ignore
        // we do not index folders, at least yet
    }

    private static String extractIndexForBookWithNoAuthor(String path) {
        return cleanup(chopOffExtension(path));
    }

    private static String extractIndexForBookWithSeriesInfo(String[] split) {
        String seriesName = split[3];
        String bookName = chopOffExtension(split[4]);
        return cleanup(bookName) + " " + seriesName;
    }

    private static String extractIndexForBookWithNoSeriesInfo(String[] split) {
        String bookName = chopOffExtension(split[3]);
        return cleanup(bookName);
    }

}
