package ru.alepar.ebook.convert;

import ru.alepar.ebook.format.EbookType;

import java.io.File;

public interface Converter {

    File convertFor(EbookType type, File file);

}
