package ru.alepar.lib.traum;

import ru.alepar.lib.fs.FileSystem;
import ru.alepar.lib.model.Author;
import ru.alepar.lib.model.Book;
import ru.alepar.lib.model.Folder;
import ru.alepar.lib.model.Item;

import java.io.File;
import java.util.regex.Pattern;

import static ru.alepar.lib.traum.PathUtils.chopOffExtension;

public class FileSystemStorage implements ItemStorage {

    private final FileSystem fs;

    public FileSystemStorage(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public Item get(String path) {
        File curFile = fs.createFile(path);
        String[] split = path.split(Pattern.quote(File.separator));

        if (!fs.exists(curFile)) {
            throw new IllegalArgumentException("path doesn't exist, path = " + path);
        }

        if (fs.isDirectory(curFile)) {
            if (split.length != 3 || "_".equals(split[1])) {
                return new Folder(path, fs.getName(curFile));
            } else {
                return new Author(path, fs.getName(curFile));
            }
        }

        String fileNameWithoutExtension = chopOffExtension(fs.getName(curFile));

        if ("_".equals(split[1])) {
            return new Book(path, fileNameWithoutExtension);
        }

        if (split.length == 4) {
            return new Book(path, fileNameWithoutExtension);
        }

        if (split.length == 5) {
            return new Book(path, makeBooknameWithSeries(split[3], fileNameWithoutExtension));
        }

        throw new IllegalArgumentException("doesnt look like traum path: " + path);
    }

    private static String makeBooknameWithSeries(String series, String bookName) {
        return bookName.replaceFirst("^([^\\d]+)\\s(.*)$", "$1 - " + series + " $2");
    }

}
