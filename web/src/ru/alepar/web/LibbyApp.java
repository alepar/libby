package ru.alepar.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.ebook.convert.CalibreConverter;
import ru.alepar.ebook.convert.Converter;
import ru.alepar.ebook.convert.Exec;
import ru.alepar.ebook.convert.JavaRuntimeExec;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.ebook.format.StaticFormatProvider;
import ru.alepar.ebook.format.UserAgentDetector;
import ru.alepar.lib.fs.FileFeeder;
import ru.alepar.lib.fs.FileSystem;
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
    - converted books cache

    - counter tests for ItemIndexer (bigger author goes first, query with author and bookname gets book as first result, not overboosted author)
    - лукьяненко дозор http://lucene.apache.org/java/3_1_0/scoring.html

    - better book series format ru/С/Серова Марина/Мисс Робин Гуд

    - paged output
 */

public class LibbyApp {

    private static final Logger log = LoggerFactory.getLogger(LibbyApp.class);

    private final Settings settings = new ResourceSettings(ResourceBundle.getBundle("/libby"));
    private final FormatProvider provider = new StaticFormatProvider();
    private final Exec exec = new JavaRuntimeExec();
    private final UserAgentDetector detector = new UserAgentDetector();
    private final Converter converter = new CalibreConverter(settings.calibreConvert(), exec, provider);

    private ItemStorage storage;
    private Index index;
    private Lister lister;
    private Querier querier;
    private FileSystem fs;

    private long indexTime;
    private int indexCount;
    private double indexSize;

    public static class Instance {

        private static final LibbyApp app = new LibbyApp();

        public static LibbyApp get() {
            return app;
        }
    }

    public LibbyApp() {
        try {
            log.info("traum.root = {}", settings.traumRoot());

            fs = new JavaFileSystem(new File(settings.traumRoot()));
            storage = new FileSystemStorage(fs);
            lister = new TraumLister(storage, fs);

            instantiateIndexes();
            reindex();

            indexSize = index.size() / 1024.0 / 1024.0;
            log.info("index takes {}MiB", String.format("%.2f", indexSize));
            querier = new Querier(index, storage);
        } catch (Exception e) {
            log.error("failed to bring up libby, terminating", e);
            System.exit(-1);
        }
    }

    private void reindex() {
        if (settings.traumReindex()) {
            log.info("reindexing");
            Date start = new Date();
            Iterable<String> feeder = new FileFeeder(".", fs);
            TraumIndexer indexer = new TraumIndexer(feeder, storage, new ItemIndexer(index, new FileSystemFileCounter(fs)));
            indexer.go();
            Date end = new Date();
            indexTime = (end.getTime() - start.getTime()) / 1000;
            indexCount = indexer.getCounter();
            log.info("reindex took {}s, added {} files", indexTime, indexCount);
        } else {
            log.warn("skipping reindex");
        }
    }

    private void instantiateIndexes() {
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

    public Iterable<Item> query(String query) {
        return querier.find(query);
    }

    public List<Item> list(String path) {
        return lister.list(path);
    }

    public File convertFile(File in, EbookType type) {
        return converter.convertFor(type, in);
    }

    public File getFile(String path) {
        return new File(settings.traumRoot(), path);
    }

    public EbookType detect(String header) {
        return detector.detect(header);
    }

    public String convertName(String name, EbookType type) {
        if (type == EbookType.DONT_CONVERT) {
            return name;
        }
        String nameWithoutExt = name.substring(0, name.indexOf("."));
        return String.format("%s.%s", nameWithoutExt, provider.extension(type));
    }

    public String status() {
        return String.format("index takes %.2fMiB, contains %d files, took %ds to build", indexSize, indexCount, indexTime);
    }
}
