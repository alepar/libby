package ru.alepar.lib.index;

public interface IndexFactory {

    BookIndex createBookIndex();

    AuthorIndex createAuthorIndex();
}
