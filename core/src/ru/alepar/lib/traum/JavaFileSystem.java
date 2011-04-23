package ru.alepar.lib.traum;

import java.io.File;

public class JavaFileSystem implements FileSystem {

    private final File basePath;

    public JavaFileSystem(File basePath) {
        this.basePath = basePath;
    }

    @Override
    public boolean exists(File curFile) {
        return curFile.exists();
    }

    @Override
    public String getName(File curFile) {
        return curFile.getName();
    }

    @Override
    public boolean isDirectory(File curFile) {
        return curFile.isDirectory();
    }

    @Override
    public File createFile(String path) {
        return new File(basePath, path);
    }

}
