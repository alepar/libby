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
    public boolean exists(String file) {
        return records.containsKey(file);
    }

    @Override
    public String getName(String file) {
        return new File(file).getName();
    }

    @Override
    public boolean isDirectory(String file) {
        return exists(file) && records.get(file).directory;
    }

    @Override
    public boolean isFile(String file) {
        return exists(file) && !records.get(file).directory;
    }

    @Override
    public String[] listFiles(String file) {
        List<String> result = new LinkedList<String>();

        for (String path : records.keySet()) {
            if (!path.equals(file) && path.startsWith(file)) {
                result.add(path);
            }
        }

        return result.toArray(new String[result.size()]);
    }

    @Override
    public String create(String base, String sub) {
        return new File(base, sub).getPath();
    }

    private static class Record {
        private final boolean directory;

        private Record(boolean directory) {
            this.directory = directory;
        }
    }
}
