package ru.alepar.lib.traum;

import java.io.File;
import java.util.regex.Pattern;

public class PathUtils {

    public static String cleanup(String path) {
        path = path.replaceAll(Pattern.quote(File.separator) + "+", " ");
        path = path.replaceAll("[^0-9a-zA-Zа-яА-Я]+", " ");
        path = path.replaceAll("\\s+", " ");
        return path.trim();
    }

    public static String chopOffExtension(String fileName) {
        return fileName.substring(0, fileName.indexOf('.'));
    }

    public static String chopOffAuthor(String bookName, String author) {
        String[] bookSplit = bookName.split(" - ");
        String[] authorSplit = author.split(" ");
        for (String word : authorSplit) {
            bookSplit[0] = chopOffWord(bookSplit[0], word);
        }
        return bookSplit[0] + bookSplit[1];
    }

    public static String chopOffWord(String source, String word) {
        return source.replaceAll(Pattern.quote(word), "");
    }

    public static String createPath(String... strs) {
        StringBuilder result = new StringBuilder();
        for (String str : strs) {
            if (result.length() > 0) {
                result.append(File.separatorChar);
            }
            result.append(str);
        }
        return result.toString();
    }
}
