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
        String choppingPart;
        String staticPart;

        if (bookName.contains(" - ")) {
            String[] bookSplit = bookName.split(" - ");
            choppingPart = bookSplit[0];
            staticPart = bookSplit[1];
        } else {
            choppingPart = bookName;
            staticPart = "";
        }

        String[] authorSplit = author.split(" ");
        for (String word : authorSplit) {
            choppingPart = chopOffWord(choppingPart, word);
        }
        return choppingPart + staticPart;
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
