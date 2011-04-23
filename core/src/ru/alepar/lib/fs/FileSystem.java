package ru.alepar.lib.fs;

import java.io.File;

public interface FileSystem {
    boolean exists(File file);

    String getName(File file);

    boolean isDirectory(File file);

    File createFile(String path);

    File[] listFiles(File file);

    boolean isFile(File file);
}
