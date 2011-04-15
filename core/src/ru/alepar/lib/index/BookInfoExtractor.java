package ru.alepar.lib.index;

import java.io.File;
import java.util.regex.Pattern;

public class BookInfoExtractor {

    public Info extract(String path) {
        String[] split = path.split(Pattern.quote(File.separator));
        String author = split[2];
        String fileName = split[3];
        return new Info(
                new Book(
                        path,
                        fileName.substring(0, fileName.indexOf('.'))
                ),
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
