package ru.alepar.lib.model;

public abstract class Item {

    public final String path;

    public Item(String path) {
        this.path = path;
    }

    public abstract void visit(ItemVisitor visitor);

    public Folder parentFolder() {
        final int firstSlashIndex = path.lastIndexOf('\\');
        if(firstSlashIndex == -1) {
            return null;
        }

        final String parentPath = path.substring(0, firstSlashIndex);
        final int secondSlashIndex = parentPath.lastIndexOf('\\');
        final String name;
        if(secondSlashIndex == -1) {
            name = parentPath;
        } else {
            name = parentPath.substring(secondSlashIndex+1, parentPath.length());
        }
        return new Folder(parentPath, name);
    }
}
