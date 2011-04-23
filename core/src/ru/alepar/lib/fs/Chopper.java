package ru.alepar.lib.fs;

import ru.alepar.lib.stuff.Oops;

import java.io.File;
import java.io.IOException;

public class Chopper {

    private final String basePath;
    private final int offset;

    public Chopper(File dir) {
        try {
            basePath = dir.getCanonicalPath();
        } catch (IOException e) {
            throw new Oops(e);
        }
        offset = basePath.endsWith(File.separator) ? 0 : 1;
    }

    public String chop(File file) {
        String path;
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {
            throw new Oops(e);
        }
        if (basePath.equals(path)) {
            return ".";
        }
        return path.substring(basePath.length() + offset);

    }

}
