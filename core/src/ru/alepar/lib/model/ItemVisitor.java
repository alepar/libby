package ru.alepar.lib.model;

public interface ItemVisitor {

    void onBook(Book book);

    void onAuthor(Author author);

    void onFolder(Folder folder);

}
