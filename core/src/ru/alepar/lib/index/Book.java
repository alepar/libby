package ru.alepar.lib.index;

public class Book {

    public final String path;
    public final String name;
    public final String seriesName;

    public Book(String path, String name, String seriesName) {
        this.path = path;
        this.name = name;
        this.seriesName = seriesName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (name != null ? !name.equals(book.name) : book.name != null) return false;
        if (path != null ? !path.equals(book.path) : book.path != null) return false;
        if (seriesName != null ? !seriesName.equals(book.seriesName) : book.seriesName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (seriesName != null ? seriesName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
