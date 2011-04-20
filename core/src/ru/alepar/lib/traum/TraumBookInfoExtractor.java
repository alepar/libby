package ru.alepar.lib.traum;

import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Book;

import java.io.File;
import java.util.regex.Pattern;

public class TraumBookInfoExtractor {

    public Info extract(String path) {
        String[] split = path.split(Pattern.quote(File.separator));

        if (split.length < 2) {
            throw new IllegalArgumentException("doesnt look like traum path: " + path);
        }

        if ("_".equals(split[1])) {
            return extractBookWithNoAuthor(path);
        }

        if (split.length == 4) {
            return extractBookWithNoSeriesInfo(path, split);
        }

        if (split.length == 5) {
            return extractBookWithSeriesInfo(path, split);
        }

        throw new IllegalArgumentException("doesnt look like traum path: " + path);
    }

    private Info extractBookWithNoAuthor(String path) {
        return new Info(
                new Book(path, cleanup(path.substring(0, path.indexOf('.'))), null),
                null
        );
    }

    private Info extractBookWithSeriesInfo(String path, String[] split) {
        String author = split[2];
        String seriesName = split[3];
        String fileName = split[4];
        return new Info(
                new Book(
                        path,
                        cleanup(chopOffAuthor(fileName.substring(0, fileName.indexOf('.')), author)),
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
                        cleanup(chopOffAuthor(fileName.substring(0, fileName.indexOf('.')), author)),
                        null),
                new Author(
                        path.substring(0, path.length() - fileName.length()),
                        author
                )
        );
    }

    private static String cleanup(String path) {
        path = path.replaceAll(Pattern.quote(File.separator) + "+", " ");
        path = path.replaceAll("[^0-9a-zA-Zа-яА-Я]+", " ");
        path = path.replaceAll("\\s+", " ");
        return path.trim();
    }

    private static String chopOffAuthor(String bookName, String author) {
        String[] split = author.split(" ");
        for (String word : split) {
            bookName = chopOffWord(bookName, word);
        }
        return bookName;
    }

    private static String chopOffWord(String source, String word) {
        return source.replaceAll(Pattern.quote(word), "");
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
