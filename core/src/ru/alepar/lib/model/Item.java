package ru.alepar.lib.model;

public abstract class Item {

    public final String path;

    public Item(String path) {
        this.path = path;
    }

    public abstract void visit(ItemVisitor visitor);
}
