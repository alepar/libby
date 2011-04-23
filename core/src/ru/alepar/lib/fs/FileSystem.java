package ru.alepar.lib.fs;

public interface FileSystem {

    boolean exists(String path);

    boolean isDirectory(String path);

    boolean isFile(String path);

    String getName(String path);

    String[] listFiles(String path);

    String create(String base, String sub);

}
