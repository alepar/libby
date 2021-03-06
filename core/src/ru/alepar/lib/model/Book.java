package ru.alepar.lib.model;

public class Book extends Item implements Comparable<Book> {

    public final String name;

    public Book(String path, String name) {
        super(path);
        this.name = name;
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.onBook(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (name != null ? !name.equals(book.name) : book.name != null) return false;
        if (path != null ? !path.equals(book.path) : book.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Book other) {
        int cmp;

        cmp = this.name.compareTo(other.name);
        if (cmp != 0) {
            return cmp;
        }

        return this.path.compareTo(other.path);
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
