package ru.alepar.lib.index;

import java.io.File;

public class Book {

    public final File file;
    public final String name;

    public Book(File file, String name) {
        this.file = file;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (file != null ? !file.equals(book.file) : book.file != null) return false;
        if (name != null ? !name.equals(book.name) : book.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = file != null ? file.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", file=" + file +
                '}';
    }
}
