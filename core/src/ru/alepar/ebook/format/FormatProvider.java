package ru.alepar.ebook.format;

public interface FormatProvider {
    String extension(EbookType type);

    String outputProfile(EbookType type);
}
