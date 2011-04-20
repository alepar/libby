package ru.alepar.lib.list;

import ru.alepar.lib.model.Book;
import ru.alepar.lib.model.Folder;
import ru.alepar.lib.model.Item;
import ru.alepar.lib.traum.Chopper;
import ru.alepar.lib.traum.TraumBookInfoExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class TraumLister implements Lister {

    private final File root;
    private final TraumBookInfoExtractor extractor;
    private final Chopper chopper;

    public TraumLister(File root, TraumBookInfoExtractor extractor) {
        this.root = root;
        this.extractor = extractor;
        this.chopper = new Chopper(root);
    }

    @Override
    public List<Item> list(String path) {
        try {
            File pathToList = new File(root, path);
            securityCheck(pathToList);

            if (!pathToList.exists()) {
                throw new RuntimeException("path does not exist");
            }
            if (!pathToList.isDirectory()) {
                throw new RuntimeException("path is not a directory");
            }

            SortedSet<Folder> folders = new TreeSet<Folder>();
            SortedSet<Book> books = new TreeSet<Book>();

            try {
                File parentFolder = new File(pathToList, "..");
                securityCheck(parentFolder);

                folders.add(new Folder(chopper.chop(parentFolder), ".."));
            } catch (RuntimeException e) {
                //ignored
            }

            for (File file : pathToList.listFiles()) {
                String relativePath = chopper.chop(file);
                if (file.isDirectory()) {
                    folders.add(new Folder(relativePath, file.getName()));
                }
                if (file.isFile()) {
                    TraumBookInfoExtractor.Info info = extractor.extract(relativePath);
                    books.add(info.book);
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
