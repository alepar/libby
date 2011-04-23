package ru.alepar.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.ebook.convert.CalibreConverter;
import ru.alepar.ebook.convert.Exec;
import ru.alepar.ebook.convert.RuntimeExec;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.ebook.format.StaticFormatProvider;
import ru.alepar.ebook.format.UserAgentDetector;
import ru.alepar.lib.index.*;
import ru.alepar.lib.list.Lister;
import ru.alepar.lib.list.TraumLister;
import ru.alepar.lib.model.Item;
import ru.alepar.lib.translit.AleparTranslit;
import ru.alepar.lib.translit.Translit;
import ru.alepar.lib.traum.*;
import ru.alepar.setting.ResourceSettings;
import ru.alepar.setting.Settings;

import java.io.File;
import java.util.*;

public class AppHolder {

    private static final Logger log = LoggerFactory.getLogger(AppHolder.class);

    private static final Settings settings = new ResourceSettings(ResourceBundle.getBundle("/libby"));
    private static final FormatProvider provider = new StaticFormatProvider();
    private static final Exec exec = new RuntimeExec();
    private static final UserAgentDetector detector = new UserAgentDetector();
    private static final Translit translit = new AleparTranslit();

    private static ItemStorage storage;
    private static Index index;
    private static CalibreConverter converter;
    private static Lister lister;


    static {
        try {
            log.info("traum.root = {}", settings.traumRoot());
            File basePath = new File(settings.traumRoot());

            JavaFileSystem fs = new JavaFileSystem(basePath);
            storage = new FileSystemStorage(fs);
            lister = new TraumLister(basePath, storage, fs);

            instantiateIndexes();
            reindex();

            converter = new CalibreConverter(settings.calibreConvert(), exec, provider);

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
        try {
            Set<String> queries = new HashSet<String>();
            queries.add(query);
            queries.addAll(translit.translate(query));

            SortedSet<String> paths = new TreeSet<String>();
            for (String q : queries) {
                paths.addAll(index.find(q));
            }

            List<Item> items = new ArrayList<Item>(paths.size());
            for (String path : paths) {
                items.add(storage.get(path));
            }
            return items;
        } catch (Exception e) {
            log.warn("query failed: " + query, e);
            throw new RuntimeException("query failed: " + query, e);
        }
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
