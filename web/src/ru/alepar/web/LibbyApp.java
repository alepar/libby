package ru.alepar.web;

import com.google.common.hash.Hashing;
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
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Set<File> convertingFiles = Collections.synchronizedSet(new HashSet<File>());
    private final File tmpdir = new File(System.getProperty("java.io.tmpdir"));

    private final ItemStorage storage;
    private final Index index;
    private final Lister lister;
    private final Querier querier;
    private final FileSystem fs;

    private final double indexSize;
    private double indexTime;
    private int indexCount;

    public File getCachedFile(String path, EbookType type) {
        if (type == EbookType.DONT_CONVERT) {
            return new File(settings.traumRoot(), path);
        }

        return new File(tmpdir, "libby-" + hash(path + " | " + type.name()) + '.' + provider.extension(type));
    }

    public File getDoneFile(File file, EbookType type) {
        if (type == EbookType.DONT_CONVERT) {
            return file;
        }

        return new File(file.getPath() + ".done");
    }

    private static String hash(String s) {
        return Hashing.md5().hashString(s, StandardCharsets.UTF_8).toString();
    }

    public LibbyApp() {
        try {
            log.info("traum.root = {}", settings.traumRoot());

            fs = new JavaFileSystem(new File(settings.traumRoot()));
            storage = new FileSystemStorage(fs);
            lister = new TraumLister(storage, fs);

            index = instantiateIndexes(settings);
            reindex();

            indexSize = index.size() / 1024.0 / 1024.0;
            log.info("index takes {}MiB", String.format("%.2f", indexSize));
            querier = new Querier(index, storage);
        } catch (Exception e) {
            log.error("failed to bring up libby, terminating", e);
            System.exit(-1);
            throw new RuntimeException(e);
        }
    }

    private void reindex() {
        if (settings.traumReindex()) {
            log.info("reindexing");
            final long start = System.nanoTime();
            Iterable<String> feeder = new FileFeeder(".", fs);
            TraumIndexer indexer = new TraumIndexer(feeder, storage, new ItemIndexer(index, new FileSystemFileCounter(fs)));
            indexer.go();
            indexTime = (System.nanoTime() - start) / 1_000_000_000.0;
            indexCount = indexer.getCounter();
            log.info("reindex took {}s, added {} files", indexTime, indexCount);
        } else {
            log.warn("skipping reindex");
        }
    }

    private static Index instantiateIndexes(Settings settings) {
        IndexFactory indexFactory;
        if (settings.traumIndex() != null) {
            log.info("using index at {}", settings.traumIndex());
            indexFactory = new FSIndexFactory(settings.traumIndex());
        } else {
            log.info("using index in RAM");
            indexFactory = new RAMIndexFactory();
        }

        return indexFactory.createIndex();
    }

    public Iterable<Item> query(String query) {
        return querier.find(query);
    }

    public List<Item> list(String path) {
        return lister.list(path);
    }

    public void convertFile(final File in, final File out, final EbookType type) {
        if (type == EbookType.DONT_CONVERT) {
            return;
        }

        if(!convertingFiles.contains(in)) {
            convertingFiles.add(in);
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        converter.convertFor(type, in, out);
                        final File doneFile = getDoneFile(out, type);
                        try {
                            final FileWriter writer = new FileWriter(doneFile);
                            try {
                                writer.write(new char[0]);
                            } finally {
                                writer.close();
                            }
                        } catch (Exception e) {
                            // failed to flag success, oh well, let's try next time
                        }
                    } finally {
                        convertingFiles.remove(in);
                    }
                }
            });
        }
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
        String nameWithoutExt = name.substring(0, name.indexOf('.'));
        return String.format("%s.%s", nameWithoutExt, provider.extension(type));
    }

    public String status() {
        return String.format("index takes %.2fMiB, contains %d files, took %.1fs to build", indexSize, indexCount, indexTime);
    }

    public static class Instance {
        private static final LibbyApp app = new LibbyApp();

        public static LibbyApp get() {
            return app;
        }
    }

}
