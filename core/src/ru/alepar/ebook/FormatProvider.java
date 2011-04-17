package ru.alepar.ebook;

import java.util.EnumMap;
import java.util.Map;

public class FormatProvider {

    private static final Map<EbookType, String> extensionsForEbook = new EnumMap<EbookType, String>(EbookType.class);
    private static final Map<EbookType, String> outputForEbook = new EnumMap<EbookType, String>(EbookType.class);

    static {
        extensionsForEbook.put(EbookType.KINDLE, "mobi");
        extensionsForEbook.put(EbookType.KINDLE_DX, "mobi");
        extensionsForEbook.put(EbookType.NOOK, "epub");

        outputForEbook.put(EbookType.KINDLE, "kindle");
        outputForEbook.put(EbookType.KINDLE_DX, "kindle_dx");
        outputForEbook.put(EbookType.NOOK, "nook");
    }

    public String extension(EbookType type) {
        return extensionsForEbook.get(type);
    }

    public String outputProfile(EbookType type) {
        return outputForEbook.get(type);
    }
}
