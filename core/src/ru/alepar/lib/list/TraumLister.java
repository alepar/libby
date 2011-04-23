package ru.alepar.lib.list;

import ru.alepar.lib.model.Folder;
import ru.alepar.lib.model.Item;
import ru.alepar.lib.traum.Chopper;
import ru.alepar.lib.traum.FileSystem;
import ru.alepar.lib.traum.ItemStorage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TraumLister implements Lister {

    private final FileSystem fs;
    private final File root;
    private final ItemStorage extractor;
    private final Chopper chopper;

    public TraumLister(File root, ItemStorage extractor, FileSystem fs) {
        this.root = root;
        this.extractor = extractor;
        this.fs = fs;
        this.chopper = new Chopper(root);
    }

    @Override
    public List<Item> list(String path) {
        try {
            File pathToList = fs.createFile(path);
            securityCheck(pathToList);

            if (!fs.exists(pathToList)) {
                throw new RuntimeException("path does not exist");
            }
            if (!fs.isDirectory(pathToList)) {
                throw new RuntimeException("path is not a directory");
            }

            SortedSet<Item> folders = new TreeSet<Item>(new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.path.compareTo(o2.path);
                }
            });
            SortedSet<Item> books = new TreeSet<Item>();

            try {
                File parentFolder = fs.createFile(path + File.separatorChar + "..");
                securityCheck(parentFolder);

                folders.add(new Folder(chopper.chop(parentFolder), ".."));
            } catch (RuntimeException e) {
                //ignored
            }

            for (File file : fs.listFiles(pathToList)) {
                String relativePath = chopper.chop(file);
                if (fs.isDirectory(file)) {
                    folders.add(extractor.get(relativePath));
                }
                if (fs.isFile(file)) {
                    books.add(extractor.get(relativePath));
                }
            }

            List<Item> result = new ArrayList<Item>(folders.size() + books.size());
            result.addAll(folders);
            result.addAll(books);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("listing failed, path= " + path, e);
        }
    }

    private void securityCheck(File pathToList) throws IOException {
        String canonicalPath = pathToList.getCanonicalPath();
        String canonicalRoot = root.getCanonicalPath();

        if (!canonicalPath.startsWith(canonicalRoot)) {
            throw new RuntimeException("path does not exist");
        }
    }

}
