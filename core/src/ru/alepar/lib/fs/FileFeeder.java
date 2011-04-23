package ru.alepar.lib.fs;

import java.io.File;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class FileFeeder implements Iterable<String> {

    private final File dir;
    private final Chopper chopper;

    public FileFeeder(File dir) {
        this.dir = dir;
        this.chopper = new Chopper(dir);
    }

    @Override
    public Iterator<String> iterator() {
        return new FeedIterator(dir, chopper);
    }

    private static class FeedIterator implements Iterator<String> {

        private final Deque<File> fileStack = new LinkedList<File>();
        private final Chopper chopper;

        private FeedIterator(File dir, Chopper chopper) {
            this.chopper = chopper;
            fileStack.push(dir);
        }

        @Override
        public boolean hasNext() {
            return !fileStack.isEmpty();
        }

        @Override
        public String next() {
            File fileOnTop = fileStack.pop();
            if (fileOnTop.isDirectory()) {
                for (String childName : fileOnTop.list()) {
                    fileStack.push(new File(fileOnTop, childName));
                }
            }
            return chopper.chop(fileOnTop);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("you wish!");
        }
    }
}
