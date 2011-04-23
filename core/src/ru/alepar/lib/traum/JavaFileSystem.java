package ru.alepar.lib.traum;

import java.io.File;

public class JavaFileSystem implements FileSystem {

    private final File basePath;

    public JavaFileSystem(File basePath) {
        this.basePath = basePath;
    }

    @Override
    public boolean exists(File file) {
        return file.exists();
    }

    @Override
    public String getName(File file) {
        return file.getName();
    }

    @Override
    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    @Override
    public File createFile(String path) {
        return new File(basePath, path);
    }

    @Override
    public File[] listFiles(File file) {
        return file.listFiles();
    }

    @Override
    public boolean isFile(File file) {
        return file.isFile();
    }
}
