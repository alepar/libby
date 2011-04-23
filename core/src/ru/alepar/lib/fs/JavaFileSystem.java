package ru.alepar.lib.fs;

import java.io.File;
import java.io.IOException;

public class JavaFileSystem implements FileSystem {

    private final File basePath;
    private final Chopper chopper;

    public JavaFileSystem(File basePath) {
        this.basePath = basePath;
        this.chopper = new Chopper(basePath);
    }

    @Override
    public boolean exists(String path) {
        File file = make(path);

        try {
            String canonicalBase = basePath.getCanonicalPath();
            String canonicalPath = file.getCanonicalPath();
            if (!canonicalPath.startsWith(canonicalBase)) {
                return false;   // security cut off
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to check if file exists", e);
        }

        return file.exists();
    }

    @Override
    public String getName(String path) {
        checkPath(path);
        return make(path).getName();
    }

    @Override
    public boolean isDirectory(String path) {
        checkPath(path);
        return make(path).isDirectory();
    }

    @Override
    public String[] listFiles(String path) {
        checkPath(path);

        File[] files = make(path).listFiles();
        String[] names = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            names[i] = chopper.chop(files[i]);

        }
        return names;
    }

    @Override
    public boolean isFile(String path) {
        checkPath(path);
        return make(path).isFile();
    }

    @Override
    public String create(String base, String sub) {
        return chopper.chop(new File(new File(basePath, base), sub));
    }

    private File make(String path) {
        return new File(basePath, path);
    }

    private void checkPath(String path) {
        if (!exists(path)) {
            throw new IllegalArgumentException("path doesnot exists, path = " + path);
        }
    }
}
