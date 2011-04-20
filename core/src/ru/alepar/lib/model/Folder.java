package ru.alepar.lib.model;

public class Folder extends Item implements Comparable<Folder> {

    public final String name;

    public Folder(String path, String name) {
        super(path);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Folder author = (Folder) o;

        if (name != null ? !name.equals(author.name) : author.name != null) return false;
        if (path != null ? !path.equals(author.path) : author.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Folder other) {
        int cmp;

        cmp = this.name.compareTo(other.name);
        if (cmp != 0) {
            return cmp;
        }

        return this.path.compareTo(other.path);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

}
