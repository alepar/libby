package ru.alepar.ebook.convert;

import ru.alepar.ebook.format.EbookType;

import java.io.File;

public interface Converter {

    void convertFor(EbookType type, File file, File out);

}
