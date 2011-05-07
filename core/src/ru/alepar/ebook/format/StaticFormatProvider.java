package ru.alepar.ebook.format;

import java.util.EnumMap;
import java.util.Map;

public class StaticFormatProvider implements FormatProvider {

    private static final Map<EbookType, String> extensionsForEbook = new EnumMap<EbookType, String>(EbookType.class);
    private static final Map<EbookType, String> outputForEbook = new EnumMap<EbookType, String>(EbookType.class);

    static {
        extensionsForEbook.put(EbookType.KINDLE, "mobi");
        extensionsForEbook.put(EbookType.KINDLE_DX, "mobi");
        extensionsForEbook.put(EbookType.NOOK, "epub");
        extensionsForEbook.put(EbookType.SONY, "lrf");

        outputForEbook.put(EbookType.KINDLE, "kindle");
        outputForEbook.put(EbookType.KINDLE_DX, "kindle_dx");
        outputForEbook.put(EbookType.NOOK, "nook");
        outputForEbook.put(EbookType.SONY, "sony");
    }

    @Override
    public String extension(EbookType type) {
        return extensionsForEbook.get(type);
    }

    @Override
    public String outputProfile(EbookType type) {
        return outputForEbook.get(type);
    }
}
