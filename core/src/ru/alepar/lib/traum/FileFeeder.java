package ru.alepar.lib.traum;

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
            drillDown();
            return !fileStack.isEmpty();
        }

        private void drillDown() {
            while (fileStack.peek() != null && !fileStack.peek().isFile()) {
                if (fileStack.isEmpty()) {
                    return;
                }
                File dir = fileStack.pop();
                for (String childName : dir.list()) {
                    fileStack.push(new File(dir, childName));
                }
            }
        }

        @Override
        public String next() {
            drillDown();
            return chopper.chop(fileStack.pop());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("you wish!");
        }
    }
}
