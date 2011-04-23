package ru.alepar.lib.list;

import ru.alepar.lib.fs.FileSystem;
import ru.alepar.lib.model.Folder;
import ru.alepar.lib.model.Item;
import ru.alepar.lib.traum.ItemStorage;

import java.util.*;

public class TraumLister implements Lister {

    private final FileSystem fs;
    private final ItemStorage extractor;

    public TraumLister(ItemStorage extractor, FileSystem fs) {
        this.extractor = extractor;
        this.fs = fs;
    }

    @Override
    public List<Item> list(String pathToList) {
        try {
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

            String parentFolder = fs.create(pathToList, "..");
            if (parentFolder != null && fs.exists(parentFolder)) {
                folders.add(new Folder(parentFolder, ".."));
            }

            for (String file : fs.listFiles(pathToList)) {
                if (fs.isDirectory(file)) {
                    folders.add(extractor.get(file));
                }
                if (fs.isFile(file)) {
                    books.add(extractor.get(file));
                }
            }

            List<Item> result = new ArrayList<Item>(folders.size() + books.size());
            result.addAll(folders);
            result.addAll(books);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("listing failed, path= " + pathToList, e);
        }
    }

}
