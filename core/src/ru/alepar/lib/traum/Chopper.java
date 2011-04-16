package ru.alepar.lib.traum;

import ru.alepar.lib.stuff.Oops;

import java.io.File;
import java.io.IOException;

public class Chopper {

    private final String basePath;

    public Chopper(File dir) {
        try {
            basePath = dir.getCanonicalPath();
        } catch (IOException e) {
            throw new Oops(e);
        }
    }

    public String chop(File file) {
        String path;
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {
            throw new Oops(e);
        }
        return path.substring(basePath.length());

    }

}
