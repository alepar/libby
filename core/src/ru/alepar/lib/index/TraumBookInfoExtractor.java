package ru.alepar.lib.index;

import java.io.File;
import java.util.regex.Pattern;

public class TraumBookInfoExtractor {

    public Info extract(String path) {
        String[] split = path.split(Pattern.quote(File.separator));

        if (split.length == 4) {
            return extractBookWithNoSeriesInfo(path, split);
        }

        if (split.length == 5) {
            return extractBookWithSeriesInfo(path, split);
        }

        return extractBookWithNoAuthor(path, split);
    }

    private Info extractBookWithNoAuthor(String path, String[] split) {
        throw new RuntimeException("alepar havent implemented me yet");
    }

    private Info extractBookWithSeriesInfo(String path, String[] split) {
        String author = split[2];
        String seriesName = split[3];
        String fileName = split[4];
        return new Info(
                new Book(
                        path,
                        fileName.substring(0, fileName.indexOf('.')),
                        seriesName),
                new Author(
                        path.substring(0, path.length() - fileName.length() - seriesName.length() - 1),
                        author
                )
        );
    }

    private Info extractBookWithNoSeriesInfo(String path, String[] split) {
        String author = split[2];
        String fileName = split[3];
        return new Info(
                new Book(
                        path,
                        fileName.substring(0, fileName.indexOf('.')),
                        null),
                new Author(
                        path.substring(0, path.length() - fileName.length()),
                        author
                )
        );
    }

    public static class Info {
        public final Book book;
        public final Author author;

        public Info(Book book, Author author) {
            this.book = book;
            this.author = author;
        }
    }
}
