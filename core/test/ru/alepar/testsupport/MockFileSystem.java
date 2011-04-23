package ru.alepar.testsupport;

import ru.alepar.lib.fs.FileSystem;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MockFileSystem implements FileSystem {

    private final Map<String, Record> records = new HashMap<String, Record>();

    public void add(String path, boolean dir) {
        records.put(path, new Record(dir));
    }

    @Override
    public boolean exists(File file) {
        return records.containsKey(file.getPath());
    }

    @Override
    public String getName(File file) {
        return file.getName();
    }

    @Override
    public boolean isDirectory(File file) {
        return exists(file) && records.get(file.getPath()).directory;
    }

    @Override
    public boolean isFile(File file) {
        return exists(file) && !records.get(file.getPath()).directory;
    }

    @Override
    public File createFile(String path) {
        return new File(path);
    }

    @Override
    public File[] listFiles(File file) {
        List<File> result = new LinkedList<File>();

        for (String path : records.keySet()) {
            if (!path.equals(file.getPath()) && path.startsWith(file.getPath())) {
                result.add(new File(path));
            }
        }

        return result.toArray(new File[result.size()]);
    }

    private static class Record {
        private final boolean directory;

        private Record(boolean directory) {
            this.directory = directory;
        }
    }
}
