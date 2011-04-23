package ru.alepar.lib.index;

import ru.alepar.lib.fs.FileSystem;

public class FileSystemFileCounter implements FileCounter {

    private final FileSystem fs;

    public FileSystemFileCounter(FileSystem fs) {
        this.fs = fs;
    }

    @Override
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
