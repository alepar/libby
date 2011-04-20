package ru.alepar.lib.list;

import ru.alepar.lib.model.Item;

import java.util.List;

public interface Lister {

    List<Item> list(String path);

}
