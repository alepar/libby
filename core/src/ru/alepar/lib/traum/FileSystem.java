package ru.alepar.lib.traum;

import java.io.File;

public interface FileSystem {
    boolean exists(File curFile);

    String getName(File curFile);

    boolean isDirectory(File curFile);

    File createFile(String path);
}
