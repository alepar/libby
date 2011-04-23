package ru.alepar.lib.index;

import ru.alepar.lib.fs.FileSystem;

public class FileCounter {

    private final FileSystem fs;

    public FileCounter(FileSystem fs) {
        this.fs = fs;
    }

    public int count(String path) {
        int count = 0;
        for (String file : fs.listFiles(path)) {
            if (fs.isFile(file)) {
                count++;
            }
            if (fs.isDirectory(file)) {
                count += count(file);
            }
        }
        return count;
    }

}
