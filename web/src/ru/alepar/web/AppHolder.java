package ru.alepar.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.ebook.convert.CalibreConverter;
import ru.alepar.ebook.convert.Exec;
import ru.alepar.ebook.convert.JavaRuntimeExec;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.ebook.format.StaticFormatProvider;
import ru.alepar.ebook.format.UserAgentDetector;
import ru.alepar.lib.fs.FileFeeder;
import ru.alepar.lib.fs.JavaFileSystem;
import ru.alepar.lib.index.*;
import ru.alepar.lib.list.Lister;
import ru.alepar.lib.list.TraumLister;
import ru.alepar.lib.model.Item;
import ru.alepar.lib.traum.FileSystemStorage;
import ru.alepar.lib.traum.ItemStorage;
import ru.alepar.lib.traum.TraumIndexer;
import ru.alepar.setting.ResourceSettings;
import ru.alepar.setting.Settings;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

// TODO wishlist
/*
    - authors get boost when indexed
    - sort authors (amount of boost?) based on number of books author has
    - series output format

    - move security stuff to FileSystem
    - replace File class with String in FileSystem interface  (TraumLister, FileFeeder)

    - paged output
 */

public class AppHolder {

    private static final Logger log = LoggerFactory.getLogger(AppHolder.class);

    private static final Settings settings = new ResourceSettings(ResourceBundle.getBundle("/libby"));
    private static final FormatProvider provider = new StaticFormatProvider();
    private static final Exec exec = new JavaRuntimeExec();
    private static final UserAgentDetector detector = new UserAgentDetector();
    private static final CalibreConverter converter = new CalibreConverter(settings.calibreConvert(), exec, provider);

    private static ItemStorage storage;
    private static Index index;
    private static Lister lister;
    private static Querier querier;


    static {
        try {
            log.info("traum.root = {}", settings.traumRoot());
            File basePath = new File(settings.traumRoot());

            JavaFileSystem fs = new JavaFileSystem(basePath);
            storage = new FileSystemStorage(fs);
            lister = new TraumLister(basePath, storage, fs);

            instantiateIndexes();
            reindex();

            querier = new Querier(index, storage);
        } catch (Exception e) {
            log.error("failed to bring up libby, terminating", e);
            System.exit(-1);
        }
    }

    private static void reindex() {
        if (settings.traumReindex()) {
            log.info("reindexing");
            Date start = new Date();
            Iterable<String> feeder = new FileFeeder(new File(settings.traumRoot()));
            TraumIndexer indexer = new TraumIndexer(feeder, storage, new ItemIndexer(index));
            indexer.go();
            Date end = new Date();
            log.info("reindex took {}s, added {} files", (end.getTime() - start.getTime()) / 1000, indexer.getCounter());
        } else {
            log.warn("skipping reindex");
        }
    }

    private static void instantiateIndexes() {
        IndexFactory indexFactory;
        if (settings.traumIndex() != null) {
            log.info("using index at {}", settings.traumIndex());
            indexFactory = new FSIndexFactory(settings.traumIndex());
        } else {
            log.info("using index in RAM");
            indexFactory = new RAMIndexFactory();
        }

        index = indexFactory.createIndex();
    }

    public static Iterable<Item> query(String query) {
        return querier.find(query);
    }

    public static List<Item> list(String path) {
        return lister.list(path);
    }

    public static File convertFile(File in, EbookType type) {
        return converter.convertFor(type, in);
    }

    public static File getFile(String path) {
        return new File(settings.traumRoot(), path);
    }

    public static EbookType detect(String header) {
        return detector.detect(header);
    }

    public static String convertName(String name, EbookType type) {
        if (type == EbookType.UNKNOWN) {
            return name;
        }
        String nameWithoutExt = name.substring(0, name.indexOf("."));
        return String.format("%s.%s", nameWithoutExt, provider.extension(type));
    }

}
