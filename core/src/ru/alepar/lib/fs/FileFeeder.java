package ru.alepar.lib.fs;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class FileFeeder implements Iterable<String> {

    private final String dir;
    private final FileSystem fs;

    public FileFeeder(String dir, FileSystem fs) {
        this.dir = dir;
        this.fs = fs;
    }

    @Override
    public Iterator<String> iterator() {
        return new FeedIterator(dir, fs);
    }

    private static class FeedIterator implements Iterator<String> {

        private final FileSystem fs;
        private final Deque<String> fileStack = new LinkedList<String>();

        private FeedIterator(String path, FileSystem fs) {
            this.fs = fs;
            fileStack.push(path);
        }

        @Override
        public boolean hasNext() {
            return !fileStack.isEmpty();
        }

        @Override
        public String next() {
            String path = fileStack.pop();
            if (fs.isDirectory(path)) {
                for (String childName : fs.listFiles(path)) {
                    fileStack.push(childName);
                }
            }
            return path;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("you wish!");
        }
    }
}
